package ru.babaetskv.passionwoman.domain.model

data class Product(
    val id: String,
    val name: String,
    val preview: Image,
    val price: Float,
    val rating: Float,
    val colors: List<ProductColor>
)
