package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.Price
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.utils.transformList

data class CartModel(
    @Json(name = "items") val items: List<CartItemModel>,
    @Json(name = "price") val price: Float,
    @Json(name = "total") val total: Float
) : Transformable<Unit, Cart> {

    override fun transform(params: Unit): Cart =
        Cart(
            items = items.transformList(),
            price = Price(price),
            total = Price(total)
        )
}
