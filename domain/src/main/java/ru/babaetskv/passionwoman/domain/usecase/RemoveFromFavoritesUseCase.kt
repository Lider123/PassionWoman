package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface RemoveFromFavoritesUseCase : ActionUseCase<String> {

    class RemoveFromFavoritesException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.REMOVE_FROM_FAVORITES_ERROR, cause)
}
