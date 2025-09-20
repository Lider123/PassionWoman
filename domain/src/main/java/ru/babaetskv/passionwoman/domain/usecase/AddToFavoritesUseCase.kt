package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface AddToFavoritesUseCase : ActionUseCase<Long> {

    class AddToFavoritesException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Action(cause, stringProvider.ADD_TO_FAVORITES_ERROR)
}
