package ru.babaetskv.passionwoman.app.presentation.feature.orderlist

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.usecase.GetOrdersUseCase

// TODO: if returns error 401 show stub view and ask for login
class OrderListViewModelImpl(
    private val getOrdersUseCase: GetOrdersUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<OrderListViewModel.Router>(dependencies), OrderListViewModel {
    private val tickerFlow = tickerFlow(DELAY_ORDER_LIST_UPDATE_MILLIS)
        .onEach {
            loadData()
        }

    override val ordersLiveData = MutableLiveData<List<Order>>()

    init {
        tickerFlow.launchIn(this)
    }

    override fun onOrderPressed(order: Order) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    private fun tickerFlow(period: Long) = flow {
        while (true) {
            emit(Unit)
            kotlinx.coroutines.delay(period)
        }
    }

    private fun loadData() {
        launchWithLoading {
            ordersLiveData.postValue(getOrdersUseCase.execute())
        }
    }

    companion object {
        private const val DELAY_ORDER_LIST_UPDATE_MILLIS = 60_000L
    }
}
