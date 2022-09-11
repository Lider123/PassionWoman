package ru.babaetskv.passionwoman.domain.model

import org.joda.time.LocalDateTime
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.OrderStatusResourcesResolver

data class Order(
    val id: Int,
    val createdAt: LocalDateTime,
    val cartItems: List<CartItem>,
    val status: Status?
) {

    fun formatCreatedAt(converter: DateTimeConverter): String =
        converter.format(createdAt, DateTimeConverter.Format.ORDER)

    enum class Status(
        val apiName: String
    ) {
        PENDING("pending"),
        IN_PROGRESS("in_progress"),
        AWAITING("awaiting"),
        COMPLETED("completed"),
        CANCELED("canceled");

        fun getColor(resolver: OrderStatusResourcesResolver): Int = resolver.getColor(this)

        fun getTitle(resolver: OrderStatusResourcesResolver): String = resolver.getTitle(this)

        companion object {

            fun fromApiName(name: String): Status? = values().find { it.apiName == name }
        }
    }
}
