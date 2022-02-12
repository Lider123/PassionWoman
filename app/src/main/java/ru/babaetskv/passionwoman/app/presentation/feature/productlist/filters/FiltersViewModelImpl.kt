package ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.GetProductsUseCase
import kotlin.coroutines.CoroutineContext

class FiltersViewModelImpl(
    private val args: FiltersFragment.Args,
    private val getProductsUseCase: GetProductsUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<FiltersViewModel.Router>(dependencies), FiltersViewModel {
    private val filterUpdatesChannel = Channel<Filter>(Channel.RENDEZVOUS)
    private val filterUpdatesFlow = filterUpdatesChannel.receiveAsFlow()
        .debounce(200L)
        .onEach(::updateFilter)

    override val filtersLiveData = MutableLiveData(args.filters)
    override val productsCountLiveData = MutableLiveData(args.initialProductsCount)

    init {
        filterUpdatesFlow.launchIn(this)
    }

    override fun onError(context: CoroutineContext, error: Throwable) {
        super.onError(context, error)
        if (error is GetProductsUseCase.GetProductsException) {
            filtersLiveData.postValue(filtersLiveData.value)
        }
    }

    override fun onFilterChanged(filter: Filter) {
        filtersLiveData.value
            ?.find { it.code == filter.code }
            ?.let {
                launch {
                    filterUpdatesChannel.send(filter)
                }
            }
    }

    override fun onApplyFiltersPressed() {
        launch {
            sendEvent(InnerEvent.UpdateFilters(filtersLiveData.value ?: args.filters))
            onBackPressed()
        }
    }

    override fun onClearFiltersPressed() {
        val newFilters = filtersLiveData.value?.map { it.clear() } ?: listOf()
        commitFilters(newFilters)
    }

    private fun updateFilter(filter: Filter) {
        val newFilters = filtersLiveData.value?.map {
            if (it.code == filter.code) filter else it
        } ?: listOf()
        commitFilters(newFilters)
    }

    private fun commitFilters(filters: List<Filter>) {
        launchWithLoading {
            val result = getProductsUseCase.execute(
                GetProductsUseCase.Params(
                    categoryId = args.categoryId,
                    limit = 1,
                    offset = 0,
                    filters = filters,
                    sorting = Sorting.DEFAULT
                ))
            productsCountLiveData.postValue(result.total)
            filtersLiveData.postValue(filters)
        }
    }
}
