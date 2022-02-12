package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

interface SortingViewModel : IViewModel {
    val stringProvider: StringProvider
    val sortingsLiveData: LiveData<List<SelectableItem<Sorting>>>

    fun onSortingPressed(item: SelectableItem<Sorting>)
    fun onApplySortingPressed()

    sealed class Router : RouterEvent
}
