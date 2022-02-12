package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface SyncFavoritesUseCase : ActionUseCase<SyncFavoritesUseCase.Params> {

    data class Params(
        val askForMerge: (doOnAnswer: MergeCallback) -> Unit,
    )

    class SyncFavoritesException(
        cause: Exception,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.SYNC_FAVORITES_ERROR, cause)
}

typealias MergeCallback = suspend (Boolean) -> Unit
