package ru.babaetskv.passionwoman.app.utils

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone Portrait",
    device = PHONE_PORTRAIT
)
@Preview(
    name = "Small Tablet Portrait",
    device = SMALL_TABLET_PORTRAIT
)
@Preview(
    name = "Large Tablet Portrait",
    device = LARGE_TABLET_PORTRAIT
)
annotation class ScreenPortraitPreviews

@Preview(
    name = "Phone Landscape",
    device = PHONE_LANDSCAPE
)
@Preview(
    name = "Small Tablet Landscape",
    device = SMALL_TABLET_LANDSCAPE
)
@Preview(
    name = "Large Tablet Landscape",
    device = LARGE_TABLET_LANDSCAPE
)
annotation class ScreenLandscapePreviews

@Preview(name = "ltr")
@Preview(
    name = "rtl",
    locale = "AR"
)
annotation class ComponentPreviews
