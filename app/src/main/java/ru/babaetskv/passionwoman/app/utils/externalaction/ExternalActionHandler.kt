package ru.babaetskv.passionwoman.app.utils.externalaction

import androidx.annotation.StringRes

interface ExternalActionHandler {
    fun handleText(text: String): Boolean
    fun handleCall(phoneNumber: String): Boolean
    fun handleCall(@StringRes phoneNumberRes: Int): Boolean
    fun handleEmail(address: String): Boolean
    fun handleEmail(@StringRes addressRes: Int): Boolean
    fun handleOuterLink(link: String): Boolean
    fun handleOuterLink(@StringRes linkRes: Int): Boolean
}
