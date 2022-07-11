package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.EmptyDataException
import ru.babaetskv.passionwoman.domain.exceptions.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetOrdersUseCase : NoParamsUseCase<List<Order>> {

    class GetOrdersException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_ORDERS_ERROR, cause)

    class EmptyOrdersException(
        stringProvider: StringProvider
    ) : EmptyDataException(stringProvider.EMPTY_ORDERS_ERROR)
}
