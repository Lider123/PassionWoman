package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase

interface GetProductUseCase : UseCase<String, Product> {

    class GetProductException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_PRODUCT_ERROR, cause)
}
