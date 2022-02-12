package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.usecase.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.utils.transformList

class GetCategoriesInteractor(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, List<Category>>(), GetCategoriesUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        GetCategoriesUseCase.GetCategoriesException(cause, stringProvider)

    override suspend fun run(params: Unit): List<Category> =
        catalogGateway.getCategories()
            .transformList()
            .also {
                if (it.isEmpty()) throw GetCategoriesUseCase.EmptyCategoriesException(stringProvider)
            }
}
