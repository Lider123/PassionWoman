package ru.babaetskv.passionwoman.domain.utils

import ru.babaetskv.passionwoman.domain.model.Price

operator fun Int.minus(other: Price): Price = Price(this - other.toFloat())

operator fun Int.times(other: Price): Price = Price(this * other.toFloat())

fun max(p1: Price, p2: Price): Price = if (p1 > p2) p1 else p2

fun min(p1: Price, p2: Price): Price = if (p1 < p2) p1 else p2
