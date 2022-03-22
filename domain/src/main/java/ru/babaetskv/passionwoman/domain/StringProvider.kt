package ru.babaetskv.passionwoman.domain

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
    val GET_FAVORITES_ERROR: String
    val GET_PRODUCT_ERROR: String
    val ADD_TO_FAVORITES_ERROR: String
    val REMOVE_FROM_FAVORITES_ERROR: String
    val SYNC_FAVORITES_ERROR: String
    val GET_PRODUCTS_PAGE_ERROR: String
    val ADD_TO_CART_ERROR: String
    val GET_CART_ITEMS_ERROR: String
    val EMPTY_CART_ITEMS_ERROR: String

    val EMPTY_CATEGORIES_ERROR: String
    val EMPTY_FAVORITES_ERROR: String
    val EMPTY_PRODUCTS_ERROR: String

    val SORT_NEW: String
    val SORT_POPULARITY: String
    val SORT_PRICE_ASC: String
    val SORT_PRICE_DESC: String

    val FILTER_DISCOUNT: String

    val GUEST_PROFILE_NAME: String
}
