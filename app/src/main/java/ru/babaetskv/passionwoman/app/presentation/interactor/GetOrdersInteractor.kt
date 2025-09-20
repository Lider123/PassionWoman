package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.app.utils.datetime.DefaultDateTimeConverter
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList
import ru.babaetskv.passionwoman.domain.usecase.GetOrdersUseCase

class GetOrdersInteractor(
    private val profileGateway: ProfileGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, List<Order>>(), GetOrdersUseCase {
    override val emptyException: UseCaseException.EmptyData =
        GetOrdersUseCase.EmptyOrdersException(stringProvider)

    override fun transformException(cause: Exception): UseCaseException =
        GetOrdersUseCase.GetOrdersException(cause, stringProvider)

    override suspend fun run(params: Unit): List<Order> =
        profileGateway.getOrders()
            .transformList(DefaultDateTimeConverter)
            .sortedByDescending(Order::createdAt)
}
