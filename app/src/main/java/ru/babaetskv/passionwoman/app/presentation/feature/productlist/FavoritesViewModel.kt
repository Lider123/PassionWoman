package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting

interface FavoritesViewModel : IViewModel {
    val stringProvider: StringProvider
    val productsLiveData: LiveData<List<Product>>
    val sortingLiveData: LiveData<Sorting>

    fun onProductPressed(product: Product)
    fun onBuyPressed(product: Product)

    sealed class Router : RouterEvent {

        data class ProductCardScreen(
            val product: Product
        ) : Router()
    }
}
