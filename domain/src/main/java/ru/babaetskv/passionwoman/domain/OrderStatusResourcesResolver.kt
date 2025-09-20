package ru.babaetskv.passionwoman.domain

import androidx.annotation.ColorInt
import ru.babaetskv.passionwoman.domain.model.Order

interface OrderStatusResourcesResolver {

    @ColorInt
    fun getColor(status: Order.Status): Int

    fun getTitle(status: Order.Status): String
}
