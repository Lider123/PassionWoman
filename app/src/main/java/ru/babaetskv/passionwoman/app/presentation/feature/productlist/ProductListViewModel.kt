package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.LiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

interface ProductListViewModel : IViewModel {
    val modeLiveData: LiveData<ProductListMode>
    val sortingLiveData: LiveData<Sorting>
    val appliedFiltersCountLiveData: LiveData<Int>
    val productsFlow: Flow<PagingData<Product>>
    val stringProvider: StringProvider

    fun onProductPressed(product: Product)
    fun onBuyPressed(product: Product)
    fun onFiltersPressed()
    fun onSortingPressed()
    fun onSearchQueryChanged(query: String)
    fun onLoadStateChanged(states: CombinedLoadStates)

    data class UpdateSortingEvent(
        val data: Sorting
    ) : Event

    data class UpdateFiltersEvent(
        val data: List<Filter>
    ) : Event

    data class OpenLandscapeProductCard(
        val product: Product
    ) : Event
}
