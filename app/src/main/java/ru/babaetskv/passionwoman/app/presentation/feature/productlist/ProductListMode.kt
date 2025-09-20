package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class ProductListMode : Parcelable {

    @Parcelize
    data class Category(
        val category: ru.babaetskv.passionwoman.domain.model.Category
    ) : ProductListMode()

    @Parcelize
    data class Specific(
        @StringRes val titleRes: Int
    ) : ProductListMode()

    @Parcelize
    object Search : ProductListMode()
}