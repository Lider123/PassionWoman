package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.EmptyDataException
import ru.babaetskv.passionwoman.domain.exceptions.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetCartItemsUseCase : NoParamsUseCase<List<CartItem>> {

    class GetCartItemsException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_CART_ITEMS_ERROR, cause)

    class EmptyCartItemsException(
        stringProvider: StringProvider
    ) : EmptyDataException(stringProvider.EMPTY_CART_ITEMS_ERROR)
}
