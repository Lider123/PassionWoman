package ru.babaetskv.passionwoman.domain.interactor.exception

interface ErrorMessageProvider {
    val GET_CATEGORIES_ERROR: String
    val GET_PRODUCTS_ERROR: String
    val AUTHORIZE_AS_GUEST_ERROR: String
    val GET_PROFILE_ERROR: String
    val AUTHORIZE_ERROR: String
    val UPDATE_PROFILE_ERROR: String
}
