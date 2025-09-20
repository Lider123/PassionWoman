package ru.babaetskv.passionwoman.app.presentation.feature.home

import androidx.annotation.StringRes
import ru.babaetskv.passionwoman.domain.model.Brand
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Promotion
import ru.babaetskv.passionwoman.domain.model.Story

sealed class HomeItem {

    data class Header(
        @StringRes val titleRes: Int,
        val isClickable: Boolean
    ) : HomeItem()

    data class Promotions(
        val data: List<Promotion>
    ) : HomeItem()

    data class Stories(
        val data: List<Story>
    ) : HomeItem()

    data class Products(
        val data: List<Product>
    ) : HomeItem()

    data class Brands(
        val data: List<Brand>
    ) : HomeItem()
}
