package ru.babaetskv.passionwoman.data.database.entity.transformations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.CartItemEntity
import ru.babaetskv.passionwoman.data.database.entity.OrderEntity
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList

class OrderTransformableParamsProvider(
    private val database: PassionWomanDatabase,
    private val exceptionProvider: ApiExceptionProvider,
    private val cartItemParamsProvider: CartItemEntity.TransformableParamsProvider
) : OrderEntity.TransformableParamsProvider {

    override suspend fun provideCartItems(orderId: Long): List<CartItemModel> =
        withContext(Dispatchers.IO) {
            return@withContext database.cartItemDao.getByOrderId(orderId)
                .takeIf { it.isNotEmpty() }
                ?.transformList(cartItemParamsProvider)
                ?: throw exceptionProvider.getNotFoundException("Cannot find cart items for the order with id $orderId")
        }
}
