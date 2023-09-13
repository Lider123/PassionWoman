package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.CheckoutResult
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class CheckoutResultModel(
    @Json(name = "orderId") val orderId: Long,
    @Json(name = "cart") val cart: CartModel,
) : Transformable<Unit, CheckoutResult>() {

    override suspend fun transform(params: Unit): CheckoutResult =
        CheckoutResult(
            orderId = orderId,
            cart = cart.transform()
        )
}