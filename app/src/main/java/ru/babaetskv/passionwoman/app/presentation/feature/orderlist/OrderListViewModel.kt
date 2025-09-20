package ru.babaetskv.passionwoman.app.presentation.feature.orderlist

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.domain.model.Order

interface OrderListViewModel : IViewModel {
    val ordersLiveData: LiveData<List<Order>>

    fun onOrderPressed(order: Order)
}
