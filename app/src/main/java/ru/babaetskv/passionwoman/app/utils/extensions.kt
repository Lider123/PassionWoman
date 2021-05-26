package ru.babaetskv.passionwoman.app.utils

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import me.philio.pinentry.PinEntryView

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun View.dip(value: Int): Int = context.dip(value)

fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun View.color(@ColorRes colorRes: Int) = context.color(colorRes)

fun Float.toPriceString() = String.format("$%.2f", this)

fun EditText.showKeyboard() {
    getSystemService(context, InputMethodManager::class.java)
        ?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard() {
    getSystemService(context, InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

fun PinEntryView.showKeyboard() {
    getSystemService(context, InputMethodManager::class.java)
        ?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun PinEntryView.hideKeyboard() {
    getSystemService(context, InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showAnimated(animation: Animation) {
    animation.apply {
        setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationRepeat(animation: Animation?) = Unit

            override fun onAnimationStart(animation: Animation?) {
                isVisible = false
            }

            override fun onAnimationEnd(animation: Animation?) {
                isVisible = true
            }
        })
    }.let {
        startAnimation(it)
    }
}

fun View.showAnimated(@AnimRes animationRes: Int) {
    showAnimated(AnimationUtils.loadAnimation(context, animationRes))
}

fun View.hideAnimated(animation: Animation) {
    Log.e("TEST", "hideAnimated") // TODO: remove
    animation.apply {
        setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationRepeat(animation: Animation?) = Unit

            override fun onAnimationStart(animation: Animation?) {
                isVisible = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.e("TEST", "Animation end") // TODO: remove
                isVisible = false
            }
        })
    }.let {
        startAnimation(it)
    }
}

fun View.hideAnimated(@AnimRes animationRes: Int) {
    hideAnimated(AnimationUtils.loadAnimation(context, animationRes))
}
