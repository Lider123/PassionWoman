package ru.babaetskv.passionwoman.app.utils

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun View.dip(value: Int): Int = context.dip(value)

fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun View.color(@ColorRes colorRes: Int) = context.color(colorRes)
