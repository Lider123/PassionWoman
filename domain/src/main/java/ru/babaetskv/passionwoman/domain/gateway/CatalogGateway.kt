package ru.babaetskv.passionwoman.domain.gateway

import ru.babaetskv.passionwoman.domain.model.*

interface CatalogGateway {

    suspend fun getCategories(): List<Category>

    suspend fun getProducts(
        categoryId: String?,
        limit: Int,
        offset: Int,
        filters: Filters,
        sorting: Sorting
    ): List<Product>

    suspend fun getPromotions(): List<Promotion>

    suspend fun getBrands(): List<Brand>
}
