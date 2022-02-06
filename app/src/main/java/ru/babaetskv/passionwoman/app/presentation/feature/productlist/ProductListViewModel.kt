package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.LiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

interface ProductListViewModel : IViewModel {
    val sortingLiveData: LiveData<Sorting>
    val appliedFiltersCountLiveData: LiveData<Int>
    val productsFlow: Flow<PagingData<Product>>
    val stringProvider: StringProvider

    fun onProductPressed(product: Product)
    fun onBuyPressed(product: Product)
    fun onFiltersPressed()
    fun onSortingPressed()
    fun onLoadStateChanged(states: CombinedLoadStates)

    sealed class Router : RouterEvent {

        data class ProductCardScreen(
            val product: Product
        ) : Router()

        data class SortingScreen(
            val selectedSorting: Sorting
        ) : Router()

        data class FiltersScreen(
            val filters: List<Filter>,
            val productsCount: Int
        ) : Router()
    }
}
