package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Sorting

class SortingViewModel(
    private val args: SortingFragment.Args,
    private val sortingUpdateHub: SortingUpdateHub,
    val stringProvider: StringProvider,
    notifier: Notifier,
    router: AppRouter
) : BaseViewModel(notifier, router) {

    val sortingsLiveData = MutableLiveData(Sorting.values().map { SortingItem(it, selected = it == args.sorting) })

    fun onSortingPressed(item: SortingItem) {
        val newValues = sortingsLiveData.value!!.map {
            it.copy(
                selected = it.sorting == item.sorting
            )
        }
        sortingsLiveData.postValue(newValues)
    }

    fun onApplySortingPressed() {
        launch {
            sortingUpdateHub.post(sortingsLiveData.value!!.find { it.selected }?.sorting ?: args.sorting)
            onBackPressed()
        }
    }
}
