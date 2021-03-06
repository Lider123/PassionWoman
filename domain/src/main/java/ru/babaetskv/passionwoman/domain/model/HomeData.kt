package ru.babaetskv.passionwoman.domain.model

data class HomeData(
    val promotions: List<Promotion>,
    val stories: List<Story>,
    val saleProducts: List<Product>,
    val popularProducts: List<Product>,
    val newProducts: List<Product>,
    val brands: List<Brand>
)
