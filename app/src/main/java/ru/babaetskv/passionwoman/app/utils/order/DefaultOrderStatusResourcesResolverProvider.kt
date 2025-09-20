package ru.babaetskv.passionwoman.app.utils.order

import android.content.Context

object DefaultOrderStatusResourcesResolverProvider {

    fun provide(context: Context): DefaultOrderStatusResourcesResolver =
        DefaultOrderStatusResourcesResolver(context)
}
