package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.EmptyDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetFavoritesUseCase : NoParamsUseCase<List<Product>> {

    class GetFavoritesException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_FAVORITES_ERROR, cause)

    class EmptyFavoritesException(
        stringProvider: StringProvider
    ) : EmptyDataException(stringProvider.EMPTY_FAVORITES_ERROR)
}
