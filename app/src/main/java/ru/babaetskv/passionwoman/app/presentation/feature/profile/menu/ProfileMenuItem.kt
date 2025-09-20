package ru.babaetskv.passionwoman.app.presentation.feature.profile.menu

import android.content.Context
import android.graphics.drawable.Drawable

interface ProfileMenuItem {

    fun getTitle(context: Context): String
    fun getIcon(context: Context): Drawable
}
