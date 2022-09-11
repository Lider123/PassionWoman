package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class OrderModel(
    @Json(name = "id") val id: Int,
    @Json(name = "createdAt") val createdAt: String,
    @Json(name = "cartItems") val cartItems: List<CartItemModel>,
    @Json(name = "status") val status: String
) : Transformable<DateTimeConverter, Order>() {

    override suspend fun transform(params: DateTimeConverter): Order =
        Order(
            id = id,
            createdAt = params.parse(createdAt, DateTimeConverter.Format.API),
            cartItems = cartItems.transformList(),
            status = Order.Status.fromApiName(status)
        )
}
