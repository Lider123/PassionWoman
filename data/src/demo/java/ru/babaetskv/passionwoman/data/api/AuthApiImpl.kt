package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import java.util.*
import kotlin.random.Random

// TODO: add token check
class AuthApiImpl(
    private val moshi: Moshi,
    private val assetManager: AssetManager,
    private val authPreferences: AuthPreferences,
    private val dateTimeConverter: DateTimeConverter
) : BaseApiImpl(), AuthApi {
    private var profileMock: ProfileModel? = null
    private var favoriteIdsMock: List<String>? = null
    private var ordersMock: MutableList<OrderModel> = mutableListOf()
    private var cartMock: CartModel = CartModel(
        items = emptyList(),
        price = 0f,
        total = 0f
    )

    override fun doBeforeRequest() {
        super.doBeforeRequest()
        if (authPreferences.authToken != TOKEN) {
            throw getUnauthorizedException("User is not authorized")
        }
    }

    override suspend fun getProfile(): ProfileModel = processRequest {
        return@processRequest if (profileMock == null) {
            loadObjectFromAsset<ProfileModel>(assetManager, AssetFile.PROFILE, moshi)
                .also { profileMock = it }
        } else profileMock!!
    }

    override suspend fun updateProfile(body: ProfileModel) = processRequest {
        profileMock = body
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) = processRequest {
        // TODO: think up how to save image
    }

    override suspend fun getFavoriteIds(): List<String> = processRequest {
        return@processRequest if (favoriteIdsMock == null) {
            loadListFromAsset<String>(assetManager, AssetFile.FAVORITE_IDS, moshi)
                .also { favoriteIdsMock = it }
        } else favoriteIdsMock!!
    }

    override suspend fun setFavoriteIds(ids: List<String>) = processRequest {
        favoriteIdsMock = ids
    }

    override suspend fun getOrders(): List<OrderModel> = processRequest {
        for (i in ordersMock.indices) {
            val newStatus = ordersMock[i].status.let(::getNextOrderStatus)
            ordersMock[i] = ordersMock[i].copy(
                status = newStatus
            )
        }
        return@processRequest ordersMock
    }

    override suspend fun checkout(): CartModel = processRequest {
        if (cartMock.items.isEmpty()) throw getBadRequestException("The cart is empty")

        val newOrder = OrderModel(
            id = UUID.randomUUID()
                .toString()
                .filter { it.isDigit() }
                .take(8),
            createdAt = DateTime.now(DateTimeZone.getDefault()).let {
                dateTimeConverter.format(it, DateTimeConverter.Format.API)
            },
            cartItems = cartMock.items,
            status = Order.Status.PENDING.apiName
        )
        ordersMock.add(newOrder)
        clearCart()
        return@processRequest cartMock
    }

    override suspend fun getCart(): CartModel = processRequest {
        return@processRequest cartMock
    }

    override suspend fun addToCart(item: CartItemModel): CartModel = processRequest {
        val items: MutableList<CartItemModel> = cartMock.items.toMutableList()
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
        cartMock = CartModel(
            items = items,
            price = calculatePrice(items),
            total = calculateTotal(items)
        )
        return@processRequest cartMock
    }

    override suspend fun removeFromCart(item: CartItemModel): CartModel = processRequest {
        val items: MutableList<CartItemModel> = cartMock.items.toMutableList()
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
            cartMock = CartModel(
                items = items,
                price = calculatePrice(items),
                total = calculateTotal(items)
            )
        }
        return@processRequest cartMock
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
        cartMock = CartModel(
            items = emptyList(),
            price = 0f,
            total = 0f
        )
    }

    companion object {
        private const val PROBABILITY_ORDER_CANCELLATION = 0.2f

        const val TOKEN = "token"
    }
}
