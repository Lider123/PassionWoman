package ru.babaetskv.passionwoman.domain.dataflow

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.base.DataFlow
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.Cart

interface CartFlow : DataFlow<Cart> {

    class EmptyCartException(
        stringProvider: StringProvider
    ) : UseCaseException.EmptyData(stringProvider.EMPTY_CART_ITEMS_ERROR)
}
