package ru.babaetskv.passionwoman.data.api.decorator.base

import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.model.*

abstract class CommonApiDecorator(
    val api: CommonApi
) : CommonApi {

    override suspend fun authorize(body: AccessTokenModel): AuthTokenModel = api.authorize(body)

    override suspend fun getCategories(): List<CategoryModel> = api.getCategories()

    override suspend fun getPopularBrands(count: Int): List<BrandModel> =
        api.getPopularBrands(count)

    override suspend fun getProduct(productId: Long): ProductModel = api.getProduct(productId)

    override suspend fun getProducts(
        categoryId: Long?,
        query: String,
        filters: String,
        sorting: String,
        limit: Int,
        offset: Int
    ): ProductsPagedResponseModel =
        api.getProducts(categoryId, query, filters, sorting, limit, offset)

    override suspend fun getProductsByIds(ids: String): List<ProductModel> =
        api.getProductsByIds(ids)

    override suspend fun getPromotions(): List<PromotionModel> = api.getPromotions()

    override suspend fun getStories(): List<StoryModel> = api.getStories()
}