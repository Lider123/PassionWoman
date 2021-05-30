package ru.babaetskv.passionwoman.domain.gateway

import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.Product

interface CatalogGateway {

    suspend fun getCategories(): List<Category>

    suspend fun getProducts(categoryId: String): List<Product>
}
