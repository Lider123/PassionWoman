package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.usecase.GetProductUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

class GetProductInteractor(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<String, Product>(), GetProductUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        GetProductUseCase.GetProductException(cause, stringProvider)

    override suspend fun run(params: String): Product =
        catalogGateway.getProduct(params).transform()
}
