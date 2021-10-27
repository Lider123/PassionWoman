package ru.babaetskv.passionwoman.app.utils

import android.content.res.Resources
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider

class StringProviderImpl(resources: Resources): StringProvider {
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

    override val EMPTY_CATEGORIES_ERROR: String = resources.getString(R.string.error_no_categories)
    override val EMPTY_FAVORITES_ERROR: String = resources.getString(R.string.error_no_favorites)
    override val EMPTY_PRODUCTS_ERROR: String = resources.getString(R.string.error_no_products)

    override val SORT_NEW: String = resources.getString(R.string.sort_new)
    override val SORT_POPULARITY: String = resources.getString(R.string.sort_popularity)
    override val SORT_PRICE_ASC: String = resources.getString(R.string.sort_price_asc)
    override val SORT_PRICE_DESC: String = resources.getString(R.string.sort_price_desc)
}
