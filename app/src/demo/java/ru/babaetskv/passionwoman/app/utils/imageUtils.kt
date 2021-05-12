package ru.babaetskv.passionwoman.app.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import ru.babaetskv.passionwoman.domain.model.Image

fun ImageView.load(image: Image, @DrawableRes placeholder: Int) {
    try {
        setImageResource(placeholder)
        val istream = context.assets.open(image.src)
        val drawable = Drawable.createFromStream(istream, null)
        setImageDrawable(drawable)
    } catch (e: Exception) {
        setImageResource(placeholder)
    }
}
