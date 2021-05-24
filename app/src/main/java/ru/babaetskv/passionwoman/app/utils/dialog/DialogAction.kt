package ru.babaetskv.passionwoman.app.utils.dialog

import androidx.appcompat.app.AlertDialog

data class DialogAction(
    val text: String,
    val isAccent: Boolean = false,
    val callback: ((dialog: AlertDialog) -> Unit)? = null
)
