package ru.babaetskv.passionwoman.app.presentation.event

import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

sealed class InnerEvent : Event {

    data class UpdateSorting(
        val data: Sorting
    ) : InnerEvent()

    data class UpdateFilters(
        val data: List<Filter>
    ) : InnerEvent()

    object UpdateProfile : InnerEvent()

    object PickCameraImage : InnerEvent()

    object PickGalleryImage : InnerEvent()

    data class AddToCart(
        val item: CartItem
    ) : InnerEvent()
}
