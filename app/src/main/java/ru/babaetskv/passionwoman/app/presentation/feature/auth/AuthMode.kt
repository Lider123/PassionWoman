package ru.babaetskv.passionwoman.app.presentation.feature.auth

import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys

enum class AuthMode(val screenName: String) {
    LOGIN(ScreenKeys.LOGIN),
    SMS_CONFIRM(ScreenKeys.SMS_CONFIRM)
}
