package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.analytics.event.SelectCategoryEvent
import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.usecase.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute

class CatalogViewModelImpl(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), CatalogViewModel {
    override val categoriesLiveData = MutableLiveData<List<Category>>(emptyList())

    init {
        loadData()
    }

    override fun onErrorActionPressed(exception: Exception) {
        when (exception) {
            is GetCategoriesUseCase.GetCategoriesException -> loadData()
            else -> super.onErrorActionPressed(exception)
        }
    }

    override fun onCategoryPressed(category: Category) {
        analyticsHandler.log(SelectCategoryEvent(category))
        router.navigateTo(ScreenProvider.category(category))
    }

    override fun onSearchPressed() {
        router.navigateTo(ScreenProvider.search())
    }

    private fun loadData() {
        launchWithLoading {
            val categories = getCategoriesUseCase.execute()
            categoriesLiveData.postValue(categories)
        }
    }
}
