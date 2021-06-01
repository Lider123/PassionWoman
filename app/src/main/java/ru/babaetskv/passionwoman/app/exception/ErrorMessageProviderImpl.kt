package ru.babaetskv.passionwoman.app.exception

import android.content.res.Resources
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider

class ErrorMessageProviderImpl(resources: Resources): ErrorMessageProvider {
    override val GET_CATEGORIES_ERROR: String = resources.getString(R.string.error_get_categories)
    override val GET_PRODUCTS_ERROR: String = resources.getString(R.string.error_get_products)
    override val AUTHORIZE_AS_GUEST_ERROR: String = resources.getString(R.string.error_auth_failed)
    override val AUTHORIZE_ERROR: String = resources.getString(R.string.error_auth_failed)
    override val GET_PROFILE_ERROR: String = resources.getString(R.string.error_get_profile)
    override val UPDATE_PROFILE_ERROR: String = resources.getString(R.string.error_update_profile)
    override val LOG_OUT_ERROR: String = resources.getString(R.string.error_log_out)
    override val UPDATE_AVATAR_ERROR: String = resources.getString(R.string.error_update_avatar)
}
