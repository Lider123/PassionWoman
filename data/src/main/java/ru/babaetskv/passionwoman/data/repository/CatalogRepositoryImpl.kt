package ru.babaetskv.passionwoman.data.repository

import ru.babaetskv.passionwoman.data.api.Api
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.repository.CatalogRepository

class CatalogRepositoryImpl(
    private val api: Api
) : CatalogRepository {

    override suspend fun getCategories(): List<Category> =
        api.getCategories().map(CategoryModel::toCategory)
}
