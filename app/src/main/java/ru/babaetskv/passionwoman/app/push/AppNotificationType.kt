package ru.babaetskv.passionwoman.app.push

enum class AppNotificationType(
    private val type: String
) {
    PRODUCT("product"),
    ORDER("order");

    companion object {

        fun getByType(type: String): AppNotificationType? =
            values().find { it.type == type }
    }
}
