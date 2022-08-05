package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkActionException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface AddToFavoritesUseCase : ActionUseCase<Int> {

    class AddToFavoritesException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.ADD_TO_FAVORITES_ERROR, cause)
}
