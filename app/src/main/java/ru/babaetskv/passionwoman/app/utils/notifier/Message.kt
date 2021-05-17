package ru.babaetskv.passionwoman.app.utils.notifier

import androidx.annotation.DrawableRes

data class Message(
    val text: String,
    @DrawableRes val iconRes: Int? = null,
    val type: Type = Type.INFO,
) {

    enum class Type {
        INFO, ERROR
    }
}
