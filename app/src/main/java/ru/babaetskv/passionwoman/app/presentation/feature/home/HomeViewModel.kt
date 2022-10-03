package ru.babaetskv.passionwoman.app.presentation.feature.home

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.domain.model.*

interface HomeViewModel : IViewModel {
    val homeItemsLiveData: LiveData<List<HomeItem>>

    fun onHeaderPressed(header: HomeItem.Header)
    fun onPromotionPressed(promotion: Promotion)
    fun onStoryPressed(story: Story)
    fun onBuyProductPressed(product: Product)
    fun onProductPressed(product: Product)
    fun onBrandPressed(brand: Brand)

    data class OpenLandscapeProductCardEvent(
        val product: Product
    ) : Event
}
