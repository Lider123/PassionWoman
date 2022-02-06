package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.model.Category

interface CatalogViewModel : IViewModel {
    val categoriesLiveData: LiveData<List<Category>>

    fun onCategoryPressed(category: Category)
    fun onSearchPressed()

    sealed class Router : RouterEvent {

        data class CategoryScreen(
            val category: Category
        ) : Router()
    }
}
