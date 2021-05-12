package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.repository.CatalogRepository

class GetCategoriesUseCase(
    private val catalogRepository: CatalogRepository
) : BaseUseCase<Unit, List<Category>>() {

    override suspend fun run(params: Unit): List<Category> =
        catalogRepository.getCategories()
}
