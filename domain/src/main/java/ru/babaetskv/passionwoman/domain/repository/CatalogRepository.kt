package ru.babaetskv.passionwoman.domain.repository

import ru.babaetskv.passionwoman.domain.model.Category

interface CatalogRepository {
    suspend fun getCategories(): List<Category>
}
