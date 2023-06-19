package ru.babaetskv.passionwoman.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okio.buffer
import okio.sink
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.CartItemEntity
import ru.babaetskv.passionwoman.data.database.entity.OrderEntity
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.random.Random

class AuthApiImpl(
    private val database: PassionWomanDatabase,
    private val exceptionProvider: ApiExceptionProvider,
    private val authPreferences: AuthPreferences,
    private val dateTimeConverter: DateTimeConverter,
    private val orderTransformableParamsProvider: OrderEntity.TransformableParamsProvider
) : AuthApi {
    private var profile: ProfileModel? = null
    private var favoriteIds: List<Long> = emptyList()
    private var cart: CartModel = CartModel(
        items = emptyList(),
        price = 0f,
        total = 0f
    )
    private val pushTokens = mutableMapOf<String, MutableSet<String>>()

    override suspend fun getProfile(): ProfileModel = withContext(Dispatchers.IO) {
        return@withContext if (profile == null) {
            database.userDao.getProfile()
                ?.transform()
                ?.also { profile = it }
                ?: throw exceptionProvider.getNotFoundException("Profile is not found")
        } else profile!!
    }

    override suspend fun updateProfile(body: ProfileModel) {
        profile = body
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) {
        // TODO: think up how to save image
    }

    override suspend fun getFavoriteIds(): List<Long> = favoriteIds

    override suspend fun setFavoriteIds(ids: List<Long>) {
        favoriteIds = ids
    }

    override suspend fun getOrders(): List<OrderModel> = withContext(Dispatchers.IO) {
        return@withContext database.orderDao.getAll()
            .transformList(orderTransformableParamsProvider)
    }

    override suspend fun checkout(): CartModel = withContext(Dispatchers.IO) {
        if (cart.items.isEmpty()) {
            throw exceptionProvider.getBadRequestException("The cart is empty")
        }

        val newOrder = OrderEntity(
            id = UUID.randomUUID()
                .toString()
                .filter { it.isDigit() }
                .take(8)
                .toLong(),
            createdAt = DateTime.now(DateTimeZone.getDefault()).let {
                dateTimeConverter.format(it, DateTimeConverter.Format.API)
            },
            status = Order.Status.PENDING.apiName
        )
        val orderId = database.orderDao.insert(newOrder)[0]
        saveCartItems(cart.items, orderId)
        clearCart()
        return@withContext cart
    }

    override suspend fun getCart(): CartModel = cart

    override suspend fun addToCart(item: CartItemModel): CartModel {
        val items: MutableList<CartItemModel> = cart.items.toMutableList()
        val existingItem = items.find {
            it.productId == item.productId
                && it.selectedSize == item.selectedSize
                && it.selectedColor == item.selectedColor
        }
        existingItem?.let {
            val position = items.indexOf(it)
            items.remove(it)
            items.add(position, it.copy(
                count = it.count + item.count
            ))
        } ?: items.add(item)
        cart = CartModel(
            items = items,
            price = calculatePrice(items),
            total = calculateTotal(items)
        )
        return cart
    }

    override suspend fun removeFromCart(item: CartItemModel): CartModel {
        val items: MutableList<CartItemModel> = cart.items.toMutableList()
        val existingItem = items.find {
            it.productId == item.productId
                    && it.selectedSize == item.selectedSize
                    && it.selectedColor == item.selectedColor
        }
        existingItem?.let {
            val position = items.indexOf(it)
            items.remove(it)
            val remainingCount = it.count - item.count
            if (remainingCount > 0) {
                items.add(position, it.copy(
                    count = remainingCount
                ))
            }
            cart = CartModel(
                items = items,
                price = calculatePrice(items),
                total = calculateTotal(items)
            )
        }
        return cart
    }

    override suspend fun registerPushToken(token: MultipartBody.Part) {
        val tokenString = readPart(token)
        val activeUserTokens: MutableSet<String> = pushTokens[authPreferences.authToken]
            ?: mutableSetOf()
        activeUserTokens.add(tokenString)
        pushTokens[authPreferences.authToken] = activeUserTokens
    }

    override suspend fun unregisterPushToken(token: MultipartBody.Part) {
        val tokenString = readPart(token)
        val activeUserTokens: MutableSet<String> = pushTokens[authPreferences.authToken]
            ?: return

        if (!activeUserTokens.contains(tokenString)) return

        activeUserTokens.remove(tokenString)
        pushTokens[authPreferences.authToken] = activeUserTokens
    }

    private suspend fun saveCartItems(items: List<CartItemModel>, orderId: Long) = withContext(Dispatchers.IO) {
        val entities = items.map { item ->
            CartItemEntity(
                orderId = orderId,
                productId = item.productId,
                preview = item.preview,
                selectedColorId = item.selectedColor.id,
                selectedSize = item.selectedSize,
                name = item.name,
                price = item.price,
                priceWithDiscount = item.priceWithDiscount,
                count = item.count
            )
        }
        database.cartItemDao.insert(*entities.toTypedArray())
    }

    private suspend fun readPart(part: MultipartBody.Part): String = withContext(Dispatchers.IO) {
        val ostream: OutputStream = ByteArrayOutputStream()
        val buffer = ostream.sink().buffer()
        part.body.writeTo(buffer)
        return@withContext ostream.toString()
    }

    private fun calculatePrice(items: List<CartItemModel>): Float =
        items.map { it.price * it.count }
            .reduceOrNull { acc, price -> acc + price } ?: 0f

    private fun calculateTotal(items: List<CartItemModel>): Float =
        items.map { it.priceWithDiscount * it.count }
            .reduceOrNull { acc, price -> acc + price } ?: 0f

    private fun getNextOrderStatus(status: String): String =
        when (Order.Status.fromApiName(status)) {
            Order.Status.PENDING -> Order.Status.IN_PROGRESS
            Order.Status.IN_PROGRESS -> {
                if (Random.nextFloat() <= PROBABILITY_ORDER_CANCELLATION) {
                    Order.Status.CANCELED
                } else Order.Status.AWAITING
            }
            Order.Status.AWAITING -> Order.Status.COMPLETED
            Order.Status.COMPLETED -> Order.Status.COMPLETED
            Order.Status.CANCELED -> Order.Status.CANCELED
            null -> null
        }?.apiName ?: status

    private fun clearCart() {
        cart = CartModel(
            items = emptyList(),
            price = 0f,
            total = 0f
        )
    }

    companion object {
        private const val PROBABILITY_ORDER_CANCELLATION = 0.2f
    }
}
