package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.model.Order
import java.util.*
import kotlin.random.Random

// TODO: add token check
class AuthApiImpl(
    private val moshi: Moshi,
    private val assetManager: AssetManager,
    private val dateTimeConverter: DateTimeConverter
) : BaseApiImpl(), AuthApi {
    private var profileMock: ProfileModel? = null
    private var favoriteIdsMock: List<String>? = null
    private var ordersMock: MutableList<OrderModel> = mutableListOf()

    override suspend fun getProfile(): ProfileModel = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext if (profileMock == null) {
            loadObjectFromAsset<ProfileModel>(assetManager, AssetFile.PROFILE, moshi)
                .also { profileMock = it }
        } else profileMock!!
    }

    override suspend fun updateProfile(body: ProfileModel) = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        profileMock = body
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        // TODO: think up how to save image
    }

    override suspend fun getFavoriteIds(): List<String> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext if (favoriteIdsMock == null) {
            loadListFromAsset<String>(assetManager, AssetFile.FAVORITE_IDS, moshi)
                .also { favoriteIdsMock = it }
        } else favoriteIdsMock!!
    }

    override suspend fun setFavoriteIds(ids: List<String>) = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        favoriteIdsMock = ids
    }

    override suspend fun getOrders(): List<OrderModel> {
        delay(DELAY_LOADING)
        for (i in ordersMock.indices) {
            val newStatus = ordersMock[i].status.let(::getNextOrderStatus)
            ordersMock[i] = ordersMock[i].copy(
                status = newStatus
            )
        }
        return ordersMock
    }

    override suspend fun checkout(items: List<CartItemModel>) {
        delay(DELAY_LOADING)
        val newOrder = OrderModel(
            id = UUID.randomUUID()
                .toString()
                .filter { it.isDigit() }
                .take(8),
            createdAt = DateTime.now(DateTimeZone.getDefault()).let {
                dateTimeConverter.format(it, DateTimeConverter.Format.API)
            },
            cartItems = items,
            status = Order.Status.PENDING.apiName
        )
        ordersMock.add(newOrder)
    }

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

    companion object {
        private const val PROBABILITY_ORDER_CANCELLATION = 0.2f
    }
}
