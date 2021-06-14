package ru.babaetskv.passionwoman.domain.model

import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider

enum class Sorting(val apiName: String) {
    NEW("new"),
    PRICE_ASC("price_asc"),
    PRICE_DESC("price_desc"),
    POPULARITY("popularity");

    fun getUiName(stringProvider: StringProvider): String = when (this) {
        NEW -> stringProvider.SORT_NEW
        PRICE_ASC -> stringProvider.SORT_PRICE_ASC
        PRICE_DESC -> stringProvider.SORT_PRICE_DESC
        POPULARITY -> stringProvider.SORT_POPULARITY
    }

    companion object {
        val DEFAULT = POPULARITY

        fun findValueByApiName(apiName: String) = values().find { it.apiName == apiName } ?: DEFAULT
    }
}
