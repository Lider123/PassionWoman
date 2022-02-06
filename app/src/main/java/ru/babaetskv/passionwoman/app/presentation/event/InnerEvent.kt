package ru.babaetskv.passionwoman.app.presentation.event

import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

sealed class InnerEvent : Event {

    data class UpdateSorting(
        val data: Sorting
    ) : InnerEvent()

    data class UpdateFilters(
        val data: List<Filter>
    ) : InnerEvent()
}
