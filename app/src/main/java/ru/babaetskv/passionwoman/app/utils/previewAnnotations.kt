package ru.babaetskv.passionwoman.app.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

private object PreviewDevices {
    const val PHONE = "spec:width=411dp,height=891dp,dpi=420"
    const val SMALL_TABLET = "spec:width=600dp,height=960dp,dpi=420"
    const val LARGE_TABLET = "spec:width=800dp,height=1280dp,dpi=420"
    const val PHONE_LANDSCAPE = "spec:width=891dp,height=411dp,dpi=420"
    const val SMALL_TABLET_LANDSCAPE = "spec:width=960dp,height=600dp,dpi=420"
    const val LARGE_TABLET_LANDSCAPE = "spec:width=1280dp,height=800dp,dpi=420"
}

@Preview(
    name = "Phone Portrait",
    device = PreviewDevices.PHONE
)
@Preview(
    name = "Phone Landscape",
    device = PreviewDevices.PHONE_LANDSCAPE
)
@Preview(
    name = "Small Tablet Portrait",
    device = PreviewDevices.SMALL_TABLET
)
@Preview(
    name = "Small Tablet Landscape",
    device = PreviewDevices.SMALL_TABLET_LANDSCAPE
)
@Preview(
    name = "Large Tablet Portrait",
    device = PreviewDevices.LARGE_TABLET
)
@Preview(
    name = "Large Tablet Landscape",
    device = PreviewDevices.LARGE_TABLET_LANDSCAPE
)
annotation class ScreenPreviews

@Preview(name = "ltr")
@Preview(
    name = "rtl",
    locale = "AR"
)
annotation class ComponentPreviews
