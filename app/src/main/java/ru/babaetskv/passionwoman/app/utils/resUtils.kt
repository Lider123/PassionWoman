package ru.babaetskv.passionwoman.app.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun View.dip(value: Int): Int = context.dip(value)

fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun View.color(@ColorRes colorRes: Int) = context.color(colorRes)

fun Context.integer(@IntegerRes intRes: Int) = resources.getInteger(intRes)

fun View.integer(@IntegerRes intRes: Int) = context.integer(intRes)

fun Context.dimen(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

fun View.dimen(@DimenRes dimenRes: Int) = context.dimen(dimenRes)

fun Context.drawable(@DrawableRes drawableRes: Int) =
    ContextCompat.getDrawable(this, drawableRes)

fun View.drawable(@DrawableRes drawableRes: Int) = context.drawable(drawableRes)

fun Context.anim(@AnimRes animationRes: Int): Animation =
    AnimationUtils.loadAnimation(this, animationRes)
