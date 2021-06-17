package ru.babaetskv.passionwoman.domain.model

enum class Sorting(val apiName: String) {
    NEW_ASC("new_asc"),
    NEW_DESC("new_desc"),
    POPULARITY_ASC("popularity_asc"),
    POPULARITY_DESC("popularity_desc");

    companion object {
        val DEFAULT = POPULARITY_DESC

        fun findValueByApiName(apiName: String) = values().find { it.apiName == apiName } ?: DEFAULT
    }
}
