package ru.babaetskv.passionwoman.app.presentation.feature.profile.menu

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import ru.babaetskv.passionwoman.app.R

class FavoritesProfileMenuItem : ProfileMenuItem {

    override fun getIcon(context: Context): Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_favorites)!!

    override fun getTitle(context: Context): String = context.getString(R.string.profile_favorites)
}
