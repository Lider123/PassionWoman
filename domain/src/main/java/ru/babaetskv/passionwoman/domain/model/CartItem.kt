package ru.babaetskv.passionwoman.domain.model

data class CartItem(
    val product: Product,
    val selectedColor: Color,
    val selectedSize: ProductSize,
    val count: Int
)
