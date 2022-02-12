package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.PassionWomanApi
import ru.babaetskv.passionwoman.data.utils.toJsonArray
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class CatalogGatewayImpl(
    private val api: PassionWomanApi
) : CatalogGateway {

    override suspend fun getCategories(): List<Transformable<Unit, Category>> =
        api.getCategories()

    override suspend fun getProducts(
        categoryId: String?,
        limit: Int,
        offset: Int,
        filters: List<Filter>,
        sorting: Sorting
    ): Transformable<StringProvider, ProductsPagedResponse> = api.getProducts(
        categoryId,
        filters.mapNotNull { it.toJsonObject() }.toJsonArray().toString(),
        sorting.apiName,
        limit,
        offset
    )

    override suspend fun getPromotions(): List<Transformable<Unit, Promotion>> =
        api.getPromotions()

    override suspend fun getPopularBrands(): List<Transformable<Unit, Brand>> =
        api.getPopularBrands()

    override suspend fun getFavorites(
        favoriteIds: Collection<String>
    ): List<Transformable<Unit, Product>> = api.getFavorites(favoriteIds.joinToString(","))

    override suspend fun getProduct(productId: String): Transformable<Unit, Product> =
        api.getProduct(productId)

    override suspend fun getFavoriteIds(): List<String> = api.getFavoriteIds()

    override suspend fun setFavoriteIds(ids: List<String>) {
        api.setFavoriteIds(ids)
    }

    override suspend fun getStories(): List<Transformable<Unit, Story>> =
        api.getStories()
}
