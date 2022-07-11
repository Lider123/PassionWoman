package ru.babaetskv.passionwoman.app.utils.order

import android.content.Context
import androidx.core.content.ContextCompat
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.domain.OrderStatusResourcesResolver
import ru.babaetskv.passionwoman.domain.model.Order

class DefaultOrderStatusResourcesResolver(
    private val context: Context
) : OrderStatusResourcesResolver {

    override fun getColor(status: Order.Status): Int = when (status) {
        Order.Status.PENDING -> R.color.order_status_pending
        Order.Status.IN_PROGRESS -> R.color.order_status_in_progress
        Order.Status.AWAITING -> R.color.order_status_awaiting
        Order.Status.COMPLETED -> R.color.order_status_completed
        Order.Status.CANCELED -> R.color.order_status_canceled
    }.let {
        ContextCompat.getColor(context, it)
    }

    override fun getTitle(status: Order.Status): String = when (status) {
        Order.Status.PENDING -> context.getString(R.string.order_status_pending)
        Order.Status.IN_PROGRESS -> context.getString(R.string.order_status_in_progress)
        Order.Status.AWAITING -> context.getString(R.string.order_status_awaiting)
        Order.Status.COMPLETED -> context.getString(R.string.order_status_completed)
        Order.Status.CANCELED -> context.getString(R.string.order_status_canceled)
    }
}
