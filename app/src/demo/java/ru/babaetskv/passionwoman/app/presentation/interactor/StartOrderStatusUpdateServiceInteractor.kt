package ru.babaetskv.passionwoman.app.presentation.interactor

import android.content.Context
import ru.babaetskv.passionwoman.app.service.OrderStatusUpdateService
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.StartOrderStatusUpdateServiceUseCase

class StartOrderStatusUpdateServiceInteractor(
    private val context: Context,
    private val stringProvider: StringProvider
) : BaseInteractor<Long, Unit>(), StartOrderStatusUpdateServiceUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        StartOrderStatusUpdateServiceUseCase.StartOrderStatusUpdateServiceException(
            cause,
            stringProvider
        )

    override suspend fun run(params: Long) {
        OrderStatusUpdateService.start(context, params)
    }
}
