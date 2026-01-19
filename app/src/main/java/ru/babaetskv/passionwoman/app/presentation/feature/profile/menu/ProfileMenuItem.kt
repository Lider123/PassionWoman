package ru.babaetskv.passionwoman.app.presentation.feature.profile.menu

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

interface ProfileMenuItem {

    fun getTitle(context: Context): String
    fun getIcon(context: Context): Drawable
    @Composable
    fun getIconPainter(): Painter
}
