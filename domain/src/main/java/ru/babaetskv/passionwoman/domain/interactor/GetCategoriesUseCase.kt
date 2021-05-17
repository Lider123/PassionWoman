package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exceptions.GetDataException
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.repository.CatalogRepository

class GetCategoriesUseCase(
    private val catalogRepository: CatalogRepository
) : BaseUseCase<Unit, List<Category>>() {

    override fun getUseCaseException(cause: Throwable): Throwable = GetCategoriesException(cause)

    override suspend fun run(params: Unit): List<Category> =
        catalogRepository.getCategories()

    private class GetCategoriesException(cause: Throwable?) : GetDataException(cause)
}
