package ru.babaetskv.passionwoman.app.utils.datetime

import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import ru.babaetskv.passionwoman.domain.DateTimeConverter

object DefaultDateTimeConverter : DateTimeConverter {
    private val DateTimeConverter.Format.formatter: DateTimeFormatter
        get() = DateTimeFormat.forPattern(pattern)

    override fun parse(
        text: String,
        format: DateTimeConverter.Format
    ): LocalDateTime = LocalDateTime.parse(text, format.formatter)

    override fun format(dateTime: LocalDateTime, format: DateTimeConverter.Format): String =
        dateTime.toString(format.formatter)

    override fun format(dateTime: DateTime, format: DateTimeConverter.Format): String =
        dateTime.toString(format.formatter)
}
