package ru.babaetskv.passionwoman.data.gateway.decorator

import ru.babaetskv.passionwoman.data.gateway.decorator.base.CatalogGatewayDecorator
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class SafeCatalogGatewayDecorator(
    gateway: CatalogGateway,
    private val exceptionProvider: GatewayExceptionProvider
) : CatalogGatewayDecorator(gateway) {

    override suspend fun getCategories(): List<Transformable<Unit, Category>> =
        try {
            super.getCategories()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getFavorites(
        favoriteIds: Collection<Long>
    ): List<Transformable<Unit, Product>> =
        try {
            super.getFavorites(favoriteIds)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getPopularBrands(count: Int): List<Transformable<Unit, Brand>> =
        try {
            super.getPopularBrands(count)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getProduct(productId: Long): Transformable<Unit, Product> =
        try {
            super.getProduct(productId)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getProducts(
        categoryId: Long?,
        query: String,
        limit: Int,
        offset: Int,
        filters: List<Filter>,
        sorting: Sorting
    ): Transformable<StringProvider, ProductsPagedResponse> =
        try {
            super.getProducts(categoryId, query, limit, offset, filters, sorting)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getPromotions(): List<Transformable<Unit, Promotion>> =
        try {
            super.getPromotions()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getStories(): List<Transformable<Unit, Story>> =
        try {
            super.getStories()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
}
