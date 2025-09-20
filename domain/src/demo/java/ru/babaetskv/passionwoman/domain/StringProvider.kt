package ru.babaetskv.passionwoman.domain

import ru.babaetskv.passionwoman.domain.model.Order

interface StringProvider : IStringProvider {
    fun getOrderNotificationBody(orderId: Long, orderStatus: Order.Status): String
}
