package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.data.order.OrderUpdatedPushSender
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.data.repository.OrdersRepository
import ru.babaetskv.passionwoman.domain.usecase.UpdateOrderStatusUseCase

class UpdateOrderStatusInteractor(
    private val repository: OrdersRepository,
    private val orderUpdatedPushSender: OrderUpdatedPushSender,
    private val stringProvider: StringProvider
) : BaseInteractor<Long, Boolean>(), UpdateOrderStatusUseCase {

    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        UpdateOrderStatusUseCase.UpdateOrderStatusException(cause, stringProvider)

    override suspend fun run(params: Long): Boolean {
        val entity = repository.getOrder(params)
            ?: throw IllegalStateException("Order with id $params not found")

        val newStatus = entity.status.let(::getNewStatus)
        if (newStatus == entity.status) return false

        val newOrder = entity.copy(status = newStatus)
        repository.updateOrder(newOrder)
        orderUpdatedPushSender.send(newOrder.id, Order.Status.fromApiName(newOrder.status)!!)
        return true
    }

    private fun getNewStatus(status: String): String =
        when (status) {
            Order.Status.PENDING.apiName -> Order.Status.IN_PROGRESS.apiName
            Order.Status.IN_PROGRESS.apiName -> Order.Status.AWAITING.apiName
            Order.Status.AWAITING.apiName -> Order.Status.COMPLETED.apiName
            Order.Status.COMPLETED.apiName, Order.Status.CANCELED.apiName -> status
            else -> status
        }
}
