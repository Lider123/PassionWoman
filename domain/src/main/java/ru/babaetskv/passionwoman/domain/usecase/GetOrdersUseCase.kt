package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetOrdersUseCase : NoParamsUseCase<List<Order>> {

    class GetOrdersException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Data(cause, stringProvider.GET_ORDERS_ERROR)

    class EmptyOrdersException(
        stringProvider: StringProvider
    ) : UseCaseException.EmptyData(stringProvider.EMPTY_ORDERS_ERROR)
}
