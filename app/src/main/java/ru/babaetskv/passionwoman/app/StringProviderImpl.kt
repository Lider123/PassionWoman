package ru.babaetskv.passionwoman.app

import android.content.res.Resources
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider

class StringProviderImpl(resources: Resources): StringProvider {
    override val GET_CATEGORIES_ERROR: String = resources.getString(R.string.error_get_categories)
    override val GET_PRODUCTS_ERROR: String = resources.getString(R.string.error_get_products)
    override val AUTHORIZE_AS_GUEST_ERROR: String = resources.getString(R.string.error_auth_failed)
    override val AUTHORIZE_ERROR: String = resources.getString(R.string.error_auth_failed)
    override val GET_PROFILE_ERROR: String = resources.getString(R.string.error_get_profile)
    override val UPDATE_PROFILE_ERROR: String = resources.getString(R.string.error_update_profile)
    override val LOG_OUT_ERROR: String = resources.getString(R.string.error_log_out)
    override val UPDATE_AVATAR_ERROR: String = resources.getString(R.string.error_update_avatar)
    override val GET_HOME_DATA_ERROR: String = resources.getString(R.string.error_get_home_data)
    override val SYNC_FAVORITES_ERROR: String = resources.getString(R.string.error_sync_favorites)

    override val SORT_NEW: String = resources.getString(R.string.sort_new)
    override val SORT_POPULARITY: String = resources.getString(R.string.sort_popularity)
    override val SORT_PRICE_ASC: String = resources.getString(R.string.sort_price_asc)
    override val SORT_PRICE_DESC: String = resources.getString(R.string.sort_price_desc)
}
