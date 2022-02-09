package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.babaetskv.passionwoman.app.R

enum class ContactsOption(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int
) {
    PHONE(R.drawable.ic_phone, R.string.contacts_phone),
    EMAIL(R.drawable.ic_email, R.string.contacts_email),
    TELEGRAM(R.drawable.ic_telegram, R.string.contacts_telegram),
    INSTAGRAM(R.drawable.ic_instagram, R.string.contacts_instagram)
}
