package ru.babaetskv.passionwoman.data.gateway

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        withContext(Dispatchers.IO) {
            return@withContext api.getCategories()
        }

    override suspend fun getProducts(
        categoryId: Long?,
        query: String,
        limit: Int,
        offset: Int,
        filters: List<Filter>,
        sorting: Sorting
    ): Transformable<StringProvider, ProductsPagedResponse> = withContext(Dispatchers.IO) {
        return@withContext api.getProducts(
            categoryId,
            query,
            filters.mapNotNull { it.toJsonObject() }.toJsonArray().toString(),
            sorting.apiName,
            limit,
            offset
        )
    }

    override suspend fun getPromotions(): List<Transformable<Unit, Promotion>> =
        withContext(Dispatchers.IO) {
            return@withContext api.getPromotions()
        }

    override suspend fun getPopularBrands(count: Int): List<Transformable<Unit, Brand>> =
        withContext(Dispatchers.IO) {
            return@withContext api.getPopularBrands(count)
        }

    override suspend fun getFavorites(
        favoriteIds: Collection<Long>
    ): List<Transformable<Unit, Product>> = withContext(Dispatchers.IO) {
        return@withContext api.getProductsByIds(favoriteIds.joinToString(","))
    }

    override suspend fun getProduct(productId: Long): Transformable<Unit, Product> =
        withContext(Dispatchers.IO) {
            return@withContext api.getProduct(productId)
        }

    override suspend fun getStories(): List<Transformable<Unit, Story>> =
        withContext(Dispatchers.IO) {
            return@withContext api.getStories()
        }
}
