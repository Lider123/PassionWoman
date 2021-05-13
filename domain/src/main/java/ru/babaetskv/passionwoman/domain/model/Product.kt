package ru.babaetskv.passionwoman.domain.model

data class Product(
    val name: String,
    val preview: Image,
    val items: List<ProductItem>
)
