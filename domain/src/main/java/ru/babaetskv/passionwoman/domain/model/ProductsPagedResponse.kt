package ru.babaetskv.passionwoman.domain.model

import ru.babaetskv.passionwoman.domain.model.base.PagedResponse
import ru.babaetskv.passionwoman.domain.model.filters.Filter

data class ProductsPagedResponse(
    override val items: List<Product>,
    override val total: Int,
    val availableFilters: List<Filter>
) : PagedResponse<Product>()
