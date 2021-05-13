package ru.babaetskv.passionwoman.domain.model

data class ProductItem(
    val color: ProductColor,
    val images: List<Image>,
    val price: Float,
    val rating: Float
)
