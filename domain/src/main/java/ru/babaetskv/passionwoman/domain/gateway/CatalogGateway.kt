package ru.babaetskv.passionwoman.domain.gateway

import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.filters.Filter

interface CatalogGateway {

    suspend fun getCategories(): List<Category>

    suspend fun getProducts(
        categoryId: String?,
        limit: Int,
        offset: Int,
        filters: List<Filter>,
        sorting: Sorting
    ): ProductsPagedResponse

    suspend fun getPromotions(): List<Promotion>

    suspend fun getPopularBrands(): List<Brand>

    suspend fun getFavorites(): List<Product>

    suspend fun getProduct(productId: String): Product

    suspend fun addToFavorites(productId: String)

    suspend fun removeFromFavorites(productId: String)

    suspend fun getFavoriteIds(): List<String>

    suspend fun setFavoriteIds(ids: List<String>)
}
