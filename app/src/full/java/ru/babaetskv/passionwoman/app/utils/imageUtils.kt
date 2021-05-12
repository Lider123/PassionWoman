package ru.babaetskv.passionwoman.app.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import ru.babaetskv.passionwoman.domain.model.Image

fun ImageView.load(image: Image, @DrawableRes placeholder: Int) {
    Glide.with(this)
        .load(image.url)
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)
}
