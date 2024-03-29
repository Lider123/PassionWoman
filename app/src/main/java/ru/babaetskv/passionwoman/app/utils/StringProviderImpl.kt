package ru.babaetskv.passionwoman.app.utils

import android.content.res.Resources
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Order

class StringProviderImpl(
    private val resources: Resources
): StringProvider {
    override val GET_CATEGORIES_ERROR: String = resources.getString(R.string.error_get_categories)
    override val GET_PRODUCTS_ERROR: String = resources.getString(R.string.error_get_products)
    override val AUTHORIZE_AS_GUEST_ERROR: String = resources.getString(R.string.error_auth_failed)
    override val AUTHORIZE_ERROR: String = resources.getString(R.string.error_auth_failed)
    override val GET_PRODUCTS_PAGE_ERROR: String = resources.getString(R.string.error_get_products_page)
    override val GET_PROFILE_ERROR: String = resources.getString(R.string.error_get_profile)
    override val UPDATE_PROFILE_ERROR: String = resources.getString(R.string.error_update_profile)
    override val LOG_OUT_ERROR: String = resources.getString(R.string.error_log_out)
    override val UPDATE_AVATAR_ERROR: String = resources.getString(R.string.error_update_avatar)
    override val GET_HOME_DATA_ERROR: String = resources.getString(R.string.error_get_home_data)
    override val GET_FAVORITES_ERROR: String = resources.getString(R.string.error_get_favorites)
    override val GET_PRODUCT_ERROR: String = resources.getString(R.string.error_get_product)
    override val ADD_TO_FAVORITES_ERROR: String = resources.getString(R.string.error_add_to_favorites)
    override val REMOVE_FROM_FAVORITES_ERROR: String = resources.getString(R.string.error_remove_from_favorites)
    override val SYNC_FAVORITES_ERROR: String = resources.getString(R.string.error_sync_favorites)
    override val ADD_TO_CART_ERROR: String = resources.getString(R.string.error_add_to_cart)
    override val REMOVE_FROM_CART_ERROR: String = resources.getString(R.string.error_remove_from_cart)
    override val GET_CART_ITEMS_ERROR: String = resources.getString(R.string.error_get_cart_items)
    override val GET_ORDERS_ERROR: String = resources.getString(R.string.error_get_orders)
    override val REGISTER_PUSH_TOKEN_ERROR: String = resources.getString(R.string.error_register_push_token)
    override val UNREGISTER_PUSH_TOKEN_ERROR: String = resources.getString(R.string.error_unregister_push_token)

    override val EMPTY_CATEGORIES_ERROR: String = resources.getString(R.string.error_no_categories)
    override val EMPTY_FAVORITES_ERROR: String = resources.getString(R.string.error_no_favorites)
    override val EMPTY_PRODUCTS_ERROR: String = resources.getString(R.string.error_no_products)
    override val EMPTY_CART_ITEMS_ERROR: String = resources.getString(R.string.error_no_cart_items)
    override val EMPTY_ORDERS_ERROR: String = resources.getString(R.string.error_no_orders)
    override val CHECKOUT_ERROR: String = resources.getString(R.string.error_checkout)

    override val CLIENT_ERROR: String = resources.getString(R.string.error_client)
    override val SERVER_ERROR: String = resources.getString(R.string.error_server)
    override val UNAUTHORIZED_ERROR: String = resources.getString(R.string.error_unauthorized)
    override val UNKNOWN_ERROR: String = resources.getString(R.string.error_unknown)

    override val SORT_NEW: String = resources.getString(R.string.sort_new)
    override val SORT_POPULARITY: String = resources.getString(R.string.sort_popularity)
    override val SORT_PRICE_ASC: String = resources.getString(R.string.sort_price_asc)
    override val SORT_PRICE_DESC: String = resources.getString(R.string.sort_price_desc)
    override val SORT_RATING: String = resources.getString(R.string.sort_rating)

    override val FILTER_DISCOUNT: String = resources.getString(R.string.filter_discount)

    override val GUEST_PROFILE_NAME: String = resources.getString(R.string.profile_guest)

    override fun getOrderNotificationBody(orderId: Long, orderStatus: Order.Status): String =
        when (orderStatus) {
            Order.Status.CANCELED -> resources.getString(R.string.order_notification_body_canceled_template, orderId.toString())
            Order.Status.IN_PROGRESS -> resources.getString(R.string.order_notification_body_in_progress_template, orderId.toString())
            Order.Status.AWAITING -> resources.getString(R.string.order_notification_body_awaiting_template, orderId.toString())
            Order.Status.COMPLETED -> resources.getString(R.string.order_notification_body_completed_template, orderId.toString())
            Order.Status.PENDING -> resources.getString(R.string.order_notification_body_pending_template, orderId.toString())
        }
}
