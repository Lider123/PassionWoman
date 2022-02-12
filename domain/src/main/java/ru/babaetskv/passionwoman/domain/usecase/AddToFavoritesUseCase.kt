package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface AddToFavoritesUseCase : ActionUseCase<String> {

    class AddToFavoritesException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.ADD_TO_FAVORITES_ERROR, cause)
}
