package ru.babaetskv.passionwoman.app.presentation.feature.home

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.filters.Filter

interface HomeViewModel : IViewModel {
    val homeItemsLiveData: LiveData<List<HomeItem>>

    fun onHeaderPressed(header: HomeItem.Header)
    fun onPromotionPressed(promotion: Promotion)
    fun onStoryPressed(story: Story)
    fun onBuyProductPressed(product: Product)
    fun onProductPressed(product: Product)
    fun onBrandPressed(brand: Brand)

    sealed class Router : RouterEvent {

        data class ProductCardScreen(
            val product: Product
        ) : Router()

        data class ProductListScreen(
            @StringRes val titleRes: Int,
            val filters: List<Filter>,
            val sorting: Sorting
        ) : Router()

        data class StoriesScreen(
            val stories: List<Story>,
            val initialStoryIndex: Int
        ) : Router()
    }
}
