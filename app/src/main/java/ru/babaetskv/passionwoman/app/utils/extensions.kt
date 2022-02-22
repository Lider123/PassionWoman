package ru.babaetskv.passionwoman.app.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import me.philio.pinentry.PinEntryView
import java.util.*

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun View.dip(value: Int): Int = context.dip(value)

fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun View.color(@ColorRes colorRes: Int) = context.color(colorRes)

fun Context.dimen(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

fun View.dimen(@DimenRes dimenRes: Int) = context.dimen(dimenRes)

fun Context.drawable(@DrawableRes drawableRes: Int) =
    ContextCompat.getDrawable(this, drawableRes)

fun View.drawable(@DrawableRes drawableRes: Int) = context.drawable(drawableRes)

fun Float.toPriceString() = String.format("$%.2f", this)

fun TextView.setHtmlText(text: String) {
    this.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

fun EditText.showKeyboard() {
    getSystemService(context, InputMethodManager::class.java)
        ?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.hideKeyboard() {
    getSystemService(requireContext(), InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(requireView().windowToken, 0)
}

fun PinEntryView.showKeyboard() {
    getSystemService(context, InputMethodManager::class.java)
        ?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun PinEntryView.hideKeyboard() {
    getSystemService(context, InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyboard() {
    val view = currentFocus ?: View(this)
    getSystemService(this, InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.showAnimated(animation: Animation, doOnAnimationEnd: (() -> Unit)? = null) {
    animation.apply {
        setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationRepeat(animation: Animation?) = Unit

            override fun onAnimationStart(animation: Animation?) {
                isVisible = false
            }

            override fun onAnimationEnd(animation: Animation?) {
                isVisible = true
                doOnAnimationEnd?.invoke()
            }
        })
    }.let {
        startAnimation(it)
    }
}

fun View.showAnimated(@AnimRes animationRes: Int, doOnAnimationEnd: (() -> Unit)? = null) {
    showAnimated(AnimationUtils.loadAnimation(context, animationRes), doOnAnimationEnd)
}

fun View.hideAnimated(animation: Animation) {
    animation.apply {
        setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationRepeat(animation: Animation?) = Unit

            override fun onAnimationStart(animation: Animation?) {
                isVisible = true
            }

            override fun onAnimationEnd(animation: Animation?) {
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

fun View.setOnSingleClickListener(callback: (v: View) -> Unit) {
    setOnClickListener(object : OnSingleClickListener() {

        override fun onSingleClick(v: View) {
            callback.invoke(v)
        }
    })
}

fun String.toFormattedPhone(): String =
    try {
        PhoneNumberUtils.formatNumber(this, Locale.getDefault().country)
    } catch (e: Exception) {
        this
    }

fun ImageView.load(@DrawableRes imageRes: Int) {
    Glide.with(this)
        .load(imageRes)
        .into(this)
}

fun View.setInsetsListener(top: Boolean = true, bottom: Boolean = true) {
    val initialPadding = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        view.updatePadding(
            top = initialPadding.top.plus(if (top) insets.systemWindowInsetTop else 0),
            bottom = initialPadding.bottom.plus(if (bottom) insets.systemWindowInsetBottom else 0)
        )
        insets
    }
}

operator fun Bundle.plus(other: Bundle): Bundle = this.apply { putAll(other) }

fun Editable?.toFloat() = this.toString().toFloat()

inline fun <T : ViewBinding> ViewGroup.viewBinding(
    crossinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    attachToRoot: Boolean = false
) = bindingInflater.invoke(LayoutInflater.from(this.context), this, attachToRoot)

fun ViewGroup.inflateLayout(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
