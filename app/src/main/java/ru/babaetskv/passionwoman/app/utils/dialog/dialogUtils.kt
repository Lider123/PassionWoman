package ru.babaetskv.passionwoman.app.utils.dialog

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.LayoutDialogBinding
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

const val DIALOG_ACTIONS_ORIENTATION_HORIZONTAL = LinearLayout.HORIZONTAL
const val DIALOG_ACTIONS_ORIENTATION_VERTICAL = LinearLayout.VERTICAL

fun Fragment.showAlertDialog(
    @StringRes messageRes: Int,
    actionsOrientation: Int = DIALOG_ACTIONS_ORIENTATION_HORIZONTAL,
    actions: List<DialogAction> = emptyList()
): AlertDialog = showAlertDialog(getString(messageRes), actionsOrientation, actions)

fun Fragment.showAlertDialog(
    message: String,
    actionsOrientation: Int = DIALOG_ACTIONS_ORIENTATION_HORIZONTAL,
    actions: List<DialogAction> = emptyList()
): AlertDialog {
    lateinit var dialog: AlertDialog
    val dialogView = View.inflate(requireContext(), R.layout.layout_dialog, null)
    val dialogViewBinding = LayoutDialogBinding.bind(dialogView)
    val buttonLayoutParams = when (actionsOrientation) {
        DIALOG_ACTIONS_ORIENTATION_HORIZONTAL -> LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            weight = 1f
        }
        DIALOG_ACTIONS_ORIENTATION_VERTICAL -> LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        else -> throw IllegalArgumentException("Unexpected value for \"actionsOrientation\": $actionsOrientation")
    }.apply {
        val margin = resources.getDimensionPixelSize(R.dimen.margin_extra_small)
        setMargins(margin)
    }
    dialogViewBinding.run {
        tvMessage.text = message
        layoutActions.orientation = actionsOrientation
        actions.forEach { action ->
            val buttonStyleAttr = if (action.isAccent) {
                R.attr.coloredButtonStyle
            } else R.attr.outlineButtonStyle
            val button = MaterialButton(requireContext(), null, buttonStyleAttr).apply {
                layoutParams = buttonLayoutParams
                text = action.text
                setOnSingleClickListener {
                    action.callback?.invoke(dialog)
                }
            }
            layoutActions.addView(button)
        }
    }
    dialog = AlertDialog.Builder(requireContext())
        .setView(dialogView)
        .create()
        .apply {
            window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
        }
    return dialog.also { it.show() }
}
