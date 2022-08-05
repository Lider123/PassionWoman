package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkActionException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface RemoveFromFavoritesUseCase : ActionUseCase<Int> {

    class RemoveFromFavoritesException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.REMOVE_FROM_FAVORITES_ERROR, cause)
}
