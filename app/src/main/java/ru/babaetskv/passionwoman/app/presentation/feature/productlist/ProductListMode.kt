package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.babaetskv.passionwoman.domain.model.Category

sealed class ProductListMode : Parcelable {

    @Parcelize
    data class CategoryMode(
        val category: Category
    ) : ProductListMode()

    @Parcelize
    data class SpecificMode(
        val title: String
    ) : ProductListMode()

    @Parcelize
    object SearchMode : ProductListMode()
}