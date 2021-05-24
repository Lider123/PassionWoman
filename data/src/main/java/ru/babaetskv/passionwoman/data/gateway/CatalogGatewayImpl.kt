package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.PassionWomanApi
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.Product

class CatalogGatewayImpl(
    private val api: PassionWomanApi
) : CatalogGateway {

    override suspend fun getCategories(): List<Category> =
        api.getCategories().map(CategoryModel::toCategory)

    override suspend fun getProducts(categoryId: String): List<Product> =
        api.getProducts(categoryId).map(ProductModel::toProduct)
}
