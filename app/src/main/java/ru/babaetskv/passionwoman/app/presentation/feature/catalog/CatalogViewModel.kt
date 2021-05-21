package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.utils.execute

class CatalogViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {
    val categoriesLiveData = MutableLiveData<List<Category>>(emptyList())

    init {
        loadData()
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        loadData()
    }

    private fun loadData() {
        launchWithLoading {
            val categories = getCategoriesUseCase.execute()
            categoriesLiveData.postValue(categories)
        }
    }

    fun onCategoryPressed(category: Category) {
        router.navigateTo(Screens.category(category))
    }
}
