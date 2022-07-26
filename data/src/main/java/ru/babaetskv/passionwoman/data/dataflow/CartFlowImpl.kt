package ru.babaetskv.passionwoman.data.dataflow

import kotlinx.coroutines.flow.*
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.model.Cart

class CartFlowImpl(
    private val stringProvider: StringProvider
) : CartFlow {
    private val flow = MutableStateFlow(Cart.EMPTY)

    override fun subscribe(): Flow<Cart> = flow

    override suspend fun send(data: Cart) {
        flow.emit(data)
    }

    override suspend fun clear() {
        flow.emit(Cart.EMPTY)
    }
}
