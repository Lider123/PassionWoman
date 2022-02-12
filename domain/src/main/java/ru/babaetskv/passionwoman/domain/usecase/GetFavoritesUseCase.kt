package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.EmptyDataException
import ru.babaetskv.passionwoman.domain.exceptions.NetworkDataException
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
