package ru.babaetskv.passionwoman.app.presentation.feature.orderlist

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetOrdersUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute

class OrderListViewModelImpl(
    private val getOrdersUseCase: GetOrdersUseCase,
    authPreferences: AuthPreferences,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), OrderListViewModel {
    private val tickerFlow = tickerFlow(DELAY_ORDER_LIST_UPDATE_MILLIS)
        .onEach {
            loadData()
        }
    private val authTypeFlow = authPreferences.authTypeFlow.onEach(::onAuthTypeUpdated)

    override val ordersLiveData = MutableLiveData<List<Order>>()

    init {
        authTypeFlow.launchIn(this)
        tickerFlow.launchIn(this)
    }

    private fun onAuthTypeUpdated(authType: AuthPreferences.AuthType) {
        loadData()
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
