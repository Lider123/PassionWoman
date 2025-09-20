package ru.babaetskv.passionwoman.app.utils.notifier

import androidx.annotation.DrawableRes

// TODO: think about adding an action button to the alert
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
