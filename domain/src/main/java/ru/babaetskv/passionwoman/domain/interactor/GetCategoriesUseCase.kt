package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.repository.CatalogRepository

class GetCategoriesUseCase(
    private val catalogRepository: CatalogRepository,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<Unit, List<Category>>() {

    override fun getUseCaseException(cause: Exception): Exception = GetCategoriesException(cause)

    override suspend fun run(params: Unit): List<Category> =
        catalogRepository.getCategories()

    private inner class GetCategoriesException(
        cause: Exception?
    ) : NetworkDataException(errorMessageProvider.GET_CATEGORIES_ERROR, cause)
}
