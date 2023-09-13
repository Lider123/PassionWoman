package ru.babaetskv.passionwoman.data.repository

import ru.babaetskv.passionwoman.data.database.entity.OrderEntity

interface OrdersRepository {

    suspend fun updateOrder(entity: OrderEntity): Long

    suspend fun getOrder(orderId: Long): OrderEntity?
}
