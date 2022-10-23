package ru.babaetskv.passionwoman.data.api.decorator

import kotlinx.coroutines.delay
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.model.*

class DelayCommonApiDecorator(api: CommonApi) : CommonApiDecorator(api) {

    override suspend fun authorize(body: AccessTokenModel): AuthTokenModel {
        delay(DELAY_LOADING)
        return api.authorize(body)
    }

    override suspend fun getCategories(): List<CategoryModel> {
        delay(DELAY_LOADING)
        return api.getCategories()
    }

    override suspend fun getPopularBrands(count: Int): List<BrandModel> {
        delay(DELAY_LOADING)
        return api.getPopularBrands(count)
    }

    override suspend fun getProduct(productId: Long): ProductModel {
        delay(DELAY_LOADING)
        return api.getProduct(productId)
    }

    override suspend fun getProducts(
        categoryId: Long?,
        query: String,
        filters: String,
        sorting: String,
        limit: Int,
        offset: Int
    ): ProductsPagedResponseModel {
        delay(DELAY_LOADING)
        return api.getProducts(categoryId, query, filters, sorting, limit, offset)
    }

    override suspend fun getProductsByIds(ids: String): List<ProductModel> {
        delay(DELAY_LOADING)
        return api.getProductsByIds(ids)
    }

    override suspend fun getPromotions(): List<PromotionModel> {
        delay(DELAY_LOADING)
        return api.getPromotions()
    }

    override suspend fun getStories(): List<StoryModel> {
        delay(DELAY_LOADING)
        return api.getStories()
    }

    companion object {
        private const val DELAY_LOADING = 500L
    }
}
