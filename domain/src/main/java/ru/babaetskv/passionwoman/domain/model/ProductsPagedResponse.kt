package ru.babaetskv.passionwoman.domain.model

import ru.babaetskv.passionwoman.domain.model.filters.Filter

data class ProductsPagedResponse(
    val products: List<Product>,
    val total: Int,
    val availableFilters: List<Filter>
)
