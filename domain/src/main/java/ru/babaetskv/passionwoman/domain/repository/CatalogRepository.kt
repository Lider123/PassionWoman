package ru.babaetskv.passionwoman.domain.repository

import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.Product

interface CatalogRepository {

    suspend fun getCategories(): List<Category>

    suspend fun getProducts(categoryId: String): List<Product>
}
