package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(
    private val value: Float = 0f
) : Parcelable, Comparable<Price> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as Price
        if (value.times(100).toInt() != other.value.times(100).toInt()) return false

        return true
    }

    override fun hashCode(): Int = value.times(100).toInt().hashCode()

    override fun compareTo(other: Price): Int =
        value.times(100).toInt().compareTo(other.value.times(100).toInt())

    operator fun div(other: Price): Price = Price(value / other.value)

    operator fun plus(other: Price): Price = Price(value + other.value)

    operator fun times(multiplier: Int): Price = Price(value * multiplier)

    fun toFormattedString(withCurrency: Boolean = true): String {
        var format = "%.2f"
        if (withCurrency) {
            format = "$$format"
        }
        return String.format(format, value)
    }

    fun toFloat(): Float = value
}
