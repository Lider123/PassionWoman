package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.utils.toJsonArray
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class CatalogGatewayImpl(
    private val api: CommonApi
) : CatalogGateway {

    override suspend fun getCategories(): List<Transformable<Unit, Category>> =
        api.getCategories()

    override suspend fun getProducts(
        categoryId: Int?,
        query: String,
        limit: Int,
        offset: Int,
        filters: List<Filter>,
        sorting: Sorting
    ): Transformable<StringProvider, ProductsPagedResponse> = api.getProducts(
        categoryId,
        query,
        filters.mapNotNull { it.toJsonObject() }.toJsonArray().toString(),
        sorting.apiName,
        limit,
        offset
    )

    override suspend fun getPromotions(): List<Transformable<Unit, Promotion>> =
        api.getPromotions()

    override suspend fun getPopularBrands(count: Int): List<Transformable<Unit, Brand>> =
        api.getPopularBrands(count)

    override suspend fun getFavorites(
        favoriteIds: Collection<Int>
    ): List<Transformable<Unit, Product>> =
        api.getProductsByIds(favoriteIds.joinToString(","))

    override suspend fun getProduct(productId: Int): Transformable<Unit, Product> =
        api.getProduct(productId)

    override suspend fun getStories(): List<Transformable<Unit, Story>> =
        api.getStories()
}
