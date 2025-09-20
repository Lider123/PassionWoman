package ru.babaetskv.passionwoman.app.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.babaetskv.passionwoman.app.R

// TODO: fix scrolling inside bottom sheet
class ContainerBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), R.style.Theme_PassionWoman_BottomSheetDialogFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_container_bottom_sheet, container, false)

    fun setContent(fragment: Fragment) {
        lifecycleScope.launchWhenStarted {
            childFragmentManager.beginTransaction()
                .replace(R.id.layout_content, fragment)
                .commit()
        }
    }

    companion object {

        fun create() = ContainerBottomSheetFragment()
    }
}
