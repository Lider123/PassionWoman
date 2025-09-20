package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListViewModel
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class SortingViewModelImpl(
    private val args: SortingFragment.Args,
    override val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), SortingViewModel {
    override val sortingsLiveData = MutableLiveData(Sorting.values().map {
        SelectableItem(it, isSelected = it == args.sorting)
    })

    override fun onSortingPressed(item: SelectableItem<Sorting>) {
        val newValues = sortingsLiveData.value!!.map {
            it.copy(
                isSelected = it.value == item.value
            )
        }
        sortingsLiveData.postValue(newValues)
    }

    override fun onApplySortingPressed() {
        launch {
            val event = ProductListViewModel.UpdateSortingEvent(sortingsLiveData.value!!.find {
                it.isSelected
            }?.value ?: args.sorting)
            sendEvent(event)
            onBackPressed()
        }
    }
}
