package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting

interface FavoritesViewModel : IViewModel {
    val stringProvider: StringProvider
    val productsLiveData: LiveData<List<Product>>
    val sortingLiveData: LiveData<Sorting>

    fun onProductPressed(product: Product)
    fun onBuyPressed(product: Product)

    data class OpenLandscapeProductCardEvent(
        val product: Product
    ) : Event
}
