package ru.babaetskv.passionwoman.app.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.domain.model.Image

fun ImageView.load(image: Image, @DrawableRes placeholder: Int, resizeAsItem: Boolean = false) {
    val sizeRes = if (resizeAsItem) R.dimen.image_size_item else R.dimen.image_size_full
    val imageSize = resources.getDimension(sizeRes).toInt()
    Glide.with(this)
        .load(image.url)
        .apply(RequestOptions().override(0, imageSize))
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)
}
