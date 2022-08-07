package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase

interface GetProductUseCase : UseCase<Int, Product> {

    class GetProductException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Data(cause, stringProvider.GET_PRODUCT_ERROR)
}
