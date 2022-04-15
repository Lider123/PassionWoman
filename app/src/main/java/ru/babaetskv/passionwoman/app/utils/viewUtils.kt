package ru.babaetskv.passionwoman.app.utils

import android.graphics.Rect
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.utils.view.OnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Image

fun TextView.setHtmlText(text: String) {
    this.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
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
    }.run(::startAnimation)
}

fun View.showAnimated(@AnimRes animationRes: Int, doOnAnimationEnd: (() -> Unit)? = null) =
    showAnimated(context.anim(animationRes), doOnAnimationEnd)

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
    }.run(::startAnimation)
}

fun View.hideAnimated(@AnimRes animationRes: Int) =
    hideAnimated(context.anim(animationRes))

fun View.setOnSingleClickListener(callback: (v: View) -> Unit) {
    setOnClickListener(object : OnSingleClickListener() {

        override fun onSingleClick(v: View) {
            callback.invoke(v)
        }
    })
}

fun ImageView.load(@DrawableRes imageRes: Int) {
    Glide.with(this)
        .load(imageRes)
        .into(this)
}

fun ImageView.load(image: Image, @DrawableRes placeholder: Int, resizeAsItem: Boolean = false) {
    val sizeRes = if (resizeAsItem) R.dimen.image_size_item else R.dimen.image_size_full
    val imageSize = resources.getDimension(sizeRes).toInt()
    Glide.with(this)
        .load(Uri.parse(image.url))
        .apply(RequestOptions().override(0, imageSize))
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)
}

fun View.setInsetsListener(
    top: Boolean = true,
    left: Boolean = true,
    right: Boolean = true,
    bottom: Boolean = true
) {
    val initialPadding = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        view.updatePadding(
            top = initialPadding.top.plus(if (top) insets.systemWindowInsetTop else 0),
            left = initialPadding.left.plus(if (left) insets.systemWindowInsetLeft else 0),
            right = initialPadding.right.plus(if (right) insets.systemWindowInsetRight else 0),
            bottom = initialPadding.bottom.plus(if (bottom) insets.systemWindowInsetBottom else 0)
        )
        insets
    }
}

inline fun <T : ViewBinding> ViewGroup.viewBinding(
    crossinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    attachToRoot: Boolean = false
) = bindingInflater.invoke(LayoutInflater.from(this.context), this, attachToRoot)

fun ViewGroup.inflateLayout(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
