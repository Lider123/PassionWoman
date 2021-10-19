package ru.babaetskv.passionwoman.app.presentation.feature.profile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.babaetskv.passionwoman.app.R

enum class ProfileMenuItem(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int
) {
    FAVORITES(R.string.profile_favorites, R.drawable.ic_favorites),
    ORDERS(R.string.profile_orders, R.drawable.ic_orders),
    CONTACTS(R.string.profile_contacts, R.drawable.ic_info)
}
