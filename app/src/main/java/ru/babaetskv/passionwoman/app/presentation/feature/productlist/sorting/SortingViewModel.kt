package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class SortingViewModel(
    private val args: SortingFragment.Args,
    private val sortingUpdateHub: SortingUpdateHub,
    val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel<SortingViewModel.Router>(dependencies) {

    val sortingsLiveData = MutableLiveData(Sorting.values().map {
        SelectableItem(it, isSelected = it == args.sorting)
    })

    fun onSortingPressed(item: SelectableItem<Sorting>) {
        val newValues = sortingsLiveData.value!!.map {
            it.copy(
                isSelected = it.value == item.value
            )
        }
        sortingsLiveData.postValue(newValues)
    }

    fun onApplySortingPressed() {
        launch {
            sortingUpdateHub.post(sortingsLiveData.value!!.find {
                it.isSelected
            }?.value ?: args.sorting)
            onBackPressed()
        }
    }

    sealed class Router : RouterEvent
}
