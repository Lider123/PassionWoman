package ru.babaetskv.passionwoman.domain.model

data class CheckoutResult(
    val orderId: Long,
    val cart: Cart
)
