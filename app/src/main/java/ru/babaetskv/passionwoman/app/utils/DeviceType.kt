package ru.babaetskv.passionwoman.app.utils

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

typealias DeviceType = String

const val PHONE_PORTRAIT: DeviceType = "spec:width=411dp,height=891dp,dpi=420"
const val PHONE_LANDSCAPE: DeviceType = "spec:width=891dp,height=411dp,dpi=420"
const val SMALL_TABLET_PORTRAIT: DeviceType = "spec:width=600dp,height=960dp,dpi=420"
const val SMALL_TABLET_LANDSCAPE: DeviceType = "spec:width=960dp,height=600dp,dpi=420"
const val LARGE_TABLET_PORTRAIT: DeviceType = "spec:width=800dp,height=1280dp,dpi=420"
const val LARGE_TABLET_LANDSCAPE: DeviceType = "spec:width=1280dp,height=800dp,dpi=420"

val WindowSizeClass.deviceType: DeviceType
    get() = when {
        widthSizeClass == WindowWidthSizeClass.Compact -> PHONE_PORTRAIT
        heightSizeClass == WindowHeightSizeClass.Compact -> PHONE_LANDSCAPE
        widthSizeClass == WindowWidthSizeClass.Medium -> SMALL_TABLET_PORTRAIT
        heightSizeClass == WindowHeightSizeClass.Medium -> SMALL_TABLET_LANDSCAPE
        widthSizeClass == WindowWidthSizeClass.Expanded -> LARGE_TABLET_PORTRAIT
        heightSizeClass == WindowHeightSizeClass.Expanded -> LARGE_TABLET_LANDSCAPE
        else -> PHONE_PORTRAIT
    }
