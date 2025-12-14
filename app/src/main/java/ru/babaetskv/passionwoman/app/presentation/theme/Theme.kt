package ru.babaetskv.passionwoman.app.presentation.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import ru.babaetskv.passionwoman.app.R

private val PassionWomanColors: Colors
    @Composable get() = lightColors(
        primary = colorResource(R.color.secondary),
        primaryVariant = colorResource(R.color.secondaryVariant),
        background = colorResource(R.color.background),
        surface = colorResource(R.color.primaryVariant),
        error = colorResource(R.color.error),
        onPrimary = colorResource(R.color.onSecondary),
        onBackground = colorResource(R.color.onPrimary),
        onSurface = colorResource(R.color.onPrimary),
        onError = colorResource(R.color.onPrimary)
    )

@Composable
fun PassionWomanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = PassionWomanColors,
        typography = PassionWomanTypography,
        shapes = PassionWomanShapes,
        content = content
    )
}
