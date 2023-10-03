package ru.babaetskv.passionwoman.app.permission

import android.Manifest
import androidx.annotation.RequiresApi

enum class Permission(
    val manifestName: String
) {
    @RequiresApi(33)
    PUSH_NOTIFICATION(Manifest.permission.POST_NOTIFICATIONS)
}
