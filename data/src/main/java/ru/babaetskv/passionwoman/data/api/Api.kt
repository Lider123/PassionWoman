package ru.babaetskv.passionwoman.data.api

import ru.babaetskv.passionwoman.data.model.CategoryModel

interface Api {

    suspend fun getCategories(): List<CategoryModel>
}
