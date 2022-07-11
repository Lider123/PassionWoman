package ru.babaetskv.passionwoman.domain

import org.joda.time.DateTime
import org.joda.time.LocalDateTime

interface DateTimeConverter {

    fun parse(text: String, format: Format): LocalDateTime

    fun format(dateTime: LocalDateTime, format: Format): String

    fun format(dateTime: DateTime, format: Format): String

    enum class Format(
        val pattern: String
    ) {
        API("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
        ORDER("dd.MM.yyyy HH:mm")
    }
}
