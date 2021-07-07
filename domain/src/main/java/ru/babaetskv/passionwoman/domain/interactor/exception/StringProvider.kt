package ru.babaetskv.passionwoman.domain.interactor.exception

interface StringProvider {
    val GET_CATEGORIES_ERROR: String
    val GET_PRODUCTS_ERROR: String
    val AUTHORIZE_AS_GUEST_ERROR: String
    val GET_PROFILE_ERROR: String
    val AUTHORIZE_ERROR: String
    val UPDATE_PROFILE_ERROR: String
    val LOG_OUT_ERROR: String
    val UPDATE_AVATAR_ERROR: String
    val GET_HOME_DATA_ERROR: String
    val SYNC_FAVORITES_ERROR: String

    val SORT_NEW: String
    val SORT_POPULARITY: String
    val SORT_PRICE_ASC: String
    val SORT_PRICE_DESC: String
}
