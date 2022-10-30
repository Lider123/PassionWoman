package ru.babaetskv.passionwoman.domain.gateway

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.model.filters.Filter

interface CatalogGateway {

    suspend fun getCategories(): List<Transformable<Unit, Category>>

    suspend fun getProducts(
        categoryId: Long?,
        query: String,
        limit: Int,
        offset: Int,
        filters: List<Filter>,
        sorting: Sorting
    ): Transformable<StringProvider, ProductsPagedResponse>

    suspend fun getPromotions(): List<Transformable<Unit, Promotion>>

    suspend fun getPopularBrands(count: Int): List<Transformable<Unit, Brand>>

    suspend fun getFavorites(favoriteIds: Collection<Long>): List<Transformable<Unit, Product>>

    suspend fun getProduct(productId: Long): Transformable<Unit, Product>

    suspend fun getStories(): List<Transformable<Unit, Story>>
}
