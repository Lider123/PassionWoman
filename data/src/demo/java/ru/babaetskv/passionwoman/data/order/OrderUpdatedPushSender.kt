package ru.babaetskv.passionwoman.data.order

import ru.babaetskv.passionwoman.domain.model.Order

interface OrderUpdatedPushSender {
    suspend fun send(orderId: Long, newStatus: Order.Status)
}
