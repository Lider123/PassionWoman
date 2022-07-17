package ru.babaetskv.passionwoman.data.dataflow

import kotlinx.coroutines.flow.MutableStateFlow
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.model.Cart

class CartFlowImpl : CartFlow {
    override val flow = MutableStateFlow(Cart.EMPTY)

    override suspend fun send(data: Cart) {
        flow.emit(data)
    }
}
