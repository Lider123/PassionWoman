package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import ru.babaetskv.passionwoman.domain.model.Sorting

@Deprecated("Use SelectableItem instead")
data class SortingItem(
    val sorting: Sorting,
    val selected: Boolean
)
