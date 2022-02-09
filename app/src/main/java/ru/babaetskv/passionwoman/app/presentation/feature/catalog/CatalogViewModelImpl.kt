package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.SelectCategoryEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.interactor.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.utils.execute

class CatalogViewModelImpl(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<CatalogViewModel.Router>(dependencies), CatalogViewModel {
    override val categoriesLiveData = MutableLiveData<List<Category>>(emptyList())

    init {
        loadData()
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        loadData()
    }

    override fun onCategoryPressed(category: Category) {
        analyticsHandler.log(SelectCategoryEvent(category))
        launch {
            navigateTo(CatalogViewModel.Router.CategoryScreen(category))
        }
    }

    override fun onSearchPressed() {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    private fun loadData() {
        launchWithLoading {
            val categories = getCategoriesUseCase.execute()
            categoriesLiveData.postValue(categories)
        }
    }
}
