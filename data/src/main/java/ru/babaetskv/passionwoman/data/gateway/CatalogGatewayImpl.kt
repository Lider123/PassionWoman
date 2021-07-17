package ru.babaetskv.passionwoman.data.gateway

import com.squareup.moshi.Moshi
import ru.babaetskv.passionwoman.data.api.PassionWomanApi
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.model.*

class CatalogGatewayImpl(
    private val api: PassionWomanApi,
    private val moshi: Moshi
) : CatalogGateway {

    override suspend fun getCategories(): List<Category> =
        api.getCategories().map(CategoryModel::toCategory)

    override suspend fun getProducts(
        categoryId: String?,
        limit: Int,
        offset: Int,
        filters: Filters,
        sorting: Sorting
    ): List<Product> = api.getProducts(
        categoryId,
        moshi.adapter(FiltersModel::class.java).toJson(FiltersModel.fromFilters(filters)),
        sorting.apiName,
        limit,
        offset
    ).map(ProductModel::toProduct)

    override suspend fun getPromotions(): List<Promotion> =
        api.getPromotions().map(PromotionModel::toPromotion)

    override suspend fun getPopularBrands(): List<Brand> =
        api.getPopularBrands().map(BrandModel::toBrand)
}
