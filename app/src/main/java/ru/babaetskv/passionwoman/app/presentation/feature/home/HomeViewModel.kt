package ru.babaetskv.passionwoman.app.presentation.feature.home

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.permission.PermissionStatus
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.domain.model.*

interface HomeViewModel : IViewModel {
    val homeItemsLiveData: LiveData<List<HomeItem>>
    val pushPermissionStatusLiveData: LiveData<PermissionStatus>

    fun onHeaderPressed(header: HomeItem.Header)
    fun onPromotionPressed(promotion: Promotion)
    fun onStoryPressed(story: Story)
    fun onBuyProductPressed(product: Product)
    fun onProductPressed(product: Product)
    fun onBrandPressed(brand: Brand)
    fun onPushPermissionRequestResult(isGranted: Boolean)
    fun onPushRationaleDialogConfirm()
    fun onPushRationaleDialogReject()

    data class OpenLandscapeProductCardEvent(
        val product: Product
    ) : Event
}
