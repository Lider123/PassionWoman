package ru.babaetskv.passionwoman.domain.model

data class ProductsPagedResponse(
    val products: List<Product>,
    val total: Int
)
