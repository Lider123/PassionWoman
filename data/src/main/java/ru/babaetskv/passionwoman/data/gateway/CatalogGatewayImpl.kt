package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.PassionWomanApi
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.data.utils.toJsonArray
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

class CatalogGatewayImpl(
    private val api: PassionWomanApi,
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : CatalogGateway {

    override suspend fun getCategories(): List<Category> =
        api.getCategories().map(CategoryModel::toCategory)
    // TODO: think about creation of ApiModel class with transform method

    override suspend fun getProducts(
        categoryId: String?,
        limit: Int,
        offset: Int,
        filters: List<Filter>,
        sorting: Sorting
    ): ProductsPagedResponse = api.getProducts(
        categoryId,
        filters.mapNotNull { it.toJsonObject() }.toJsonArray().toString(),
        sorting.apiName,
        limit,
        offset
    ).toProductPagedResponse(stringProvider)

    override suspend fun getPromotions(): List<Promotion> =
        api.getPromotions().map(PromotionModel::toPromotion)

    override suspend fun getPopularBrands(): List<Brand> =
        api.getPopularBrands().map(BrandModel::toBrand)

    override suspend fun getFavorites(): List<Product> =
        api.getFavorites(favoritesPreferences.getFavoriteIds().joinToString(","))
            .map(ProductModel::toProduct)

    override suspend fun getProduct(productId: String): Product =
        api.getProduct(productId).toProduct()

    override suspend fun addToFavorites(productId: String) {
        favoritesPreferences.putFavoriteId(productId)
    }

    override suspend fun removeFromFavorites(productId: String) {
        favoritesPreferences.deleteFavoriteId(productId)
    }

    override suspend fun getFavoriteIds(): List<String> = api.getFavoriteIds()

    override suspend fun setFavoriteIds(ids: List<String>) {
        api.setFavoriteIds(ids)
    }

    override suspend fun getStories(): List<Story> =
        api.getStories().map(StoryModel::toStory)
}
