package ru.babaetskv.passionwoman.app.utils.notifier

import androidx.annotation.DrawableRes

data class AlertMessage(
    val text: String,
    @DrawableRes val iconRes: Int? = null,
    val type: Type = Type.INFO,
    val isImportant: Boolean = true
) {

    enum class Type {
        INFO, ERROR
    }
}
