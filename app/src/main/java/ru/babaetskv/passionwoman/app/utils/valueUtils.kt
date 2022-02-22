package ru.babaetskv.passionwoman.app.utils

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.Editable
import java.util.*

fun String.toFormattedPhone(): String =
    try {
        PhoneNumberUtils.formatNumber(this, Locale.getDefault().country)
    } catch (e: Exception) {
        this
    }

operator fun Bundle.plus(other: Bundle): Bundle = apply { putAll(other) }

fun Editable?.toFloat() = toString().toFloat()
