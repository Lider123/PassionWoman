package ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.model.filters.Filter

interface FiltersViewModel : IViewModel {
    val filtersLiveData: LiveData<List<Filter>>
    val productsCountLiveData: LiveData<Int>

    fun onFilterChanged(filter: Filter)
    fun onApplyFiltersPressed()
    fun onClearFiltersPressed()

    class Router : RouterEvent
}
