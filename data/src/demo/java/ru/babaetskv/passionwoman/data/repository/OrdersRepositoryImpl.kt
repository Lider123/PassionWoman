package ru.babaetskv.passionwoman.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.OrderEntity

class OrdersRepositoryImpl(
    private val database: PassionWomanDatabase
) : OrdersRepository {

    override suspend fun updateOrder(entity: OrderEntity): Long = withContext(Dispatchers.IO) {
        database.orderDao.update(entity)
        return@withContext database.orderDao.getById(entity.id)!!.id
    }

    override suspend fun getOrder(orderId: Long): OrderEntity? = withContext(Dispatchers.IO) {
        return@withContext database.orderDao.getById(orderId)
    }
}
