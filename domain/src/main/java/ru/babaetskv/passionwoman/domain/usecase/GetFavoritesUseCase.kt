package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetFavoritesUseCase : NoParamsUseCase<List<Product>> {

    class GetFavoritesException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Data(cause, stringProvider.GET_FAVORITES_ERROR)

    class EmptyFavoritesException(
        stringProvider: StringProvider
    ) : UseCaseException.EmptyData(stringProvider.EMPTY_FAVORITES_ERROR)
}
