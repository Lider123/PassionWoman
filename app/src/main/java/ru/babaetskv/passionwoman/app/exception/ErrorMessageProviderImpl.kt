package ru.babaetskv.passionwoman.app.exception

import android.content.res.Resources
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider

class ErrorMessageProviderImpl(resources: Resources): ErrorMessageProvider {
    override val GET_CATEGORIES_ERROR: String = resources.getString(R.string.error_get_categories)
    override val GET_PRODUCTS_ERROR: String = resources.getString(R.string.error_get_products)
}
