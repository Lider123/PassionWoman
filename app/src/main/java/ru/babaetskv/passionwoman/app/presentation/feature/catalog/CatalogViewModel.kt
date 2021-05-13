package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.domain.interactor.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.utils.execute

class CatalogViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    router: Router
) : BaseViewModel(router) {
    val categoriesLiveData = MutableLiveData<List<Category>>(emptyList())

    init {
        loadData()
    }

    private fun loadData() {
        launch {
            when (val result = getCategoriesUseCase.execute()) {
                is BaseUseCase.Result.Success -> {
                    categoriesLiveData.postValue(result.data)
                }
                is BaseUseCase.Result.Failure -> {
                    // TODO: handle error
                }
            }
        }
    }

    fun onCategoryPressed(category: Category) {
        router.navigateTo(Screens.category(category))
    }
}
