package ru.babaetskv.passionwoman.data.gateway.decorator.base

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.model.filters.Filter

abstract class CatalogGatewayDecorator(
    val gateway: CatalogGateway
) : CatalogGateway {

    override suspend fun getCategories(): List<Transformable<Unit, Category>> =
        gateway.getCategories()

    override suspend fun getFavorites(
        favoriteIds: Collection<Long>
    ): List<Transformable<Unit, Product>> = getFavorites(favoriteIds)

    override suspend fun getPopularBrands(count: Int): List<Transformable<Unit, Brand>> =
        gateway.getPopularBrands(count)

    override suspend fun getProduct(productId: Long): Transformable<Unit, Product> =
        getProduct(productId)

    override suspend fun getProducts(
        categoryId: Long?,
        query: String,
        limit: Int,
        offset: Int,
        filters: List<Filter>,
        sorting: Sorting
    ): Transformable<StringProvider, ProductsPagedResponse> =
        gateway.getProducts(categoryId, query, limit, offset, filters, sorting)

    override suspend fun getPromotions(): List<Transformable<Unit, Promotion>> =
        gateway.getPromotions()

    override suspend fun getStories(): List<Transformable<Unit, Story>> = gateway.getStories()
}
