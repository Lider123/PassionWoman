package ru.babaetskv.passionwoman.app.presentation.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.babaetskv.passionwoman.app.R
import timber.log.Timber

abstract class BaseBottomSheetDialogFragment<VM : IViewModel, TArgs : Parcelable> :
    BottomSheetDialogFragment(),
    FragmentComponent<VM, TArgs> {
    override var componentArguments: Bundle
        get() = requireArguments()
        set(value) {
            arguments = value
        }
    override val componentContext: Context
        get() = requireContext()
    override var _args: TArgs? = null
    override val componentView: View
        get() = requireView()
    override val componentViewLifecycleOwner: LifecycleOwner
        get() = viewLifecycleOwner

    abstract val layoutRes: Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), R.style.Theme_PassionWoman_BottomSheetDialogFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart(screenName)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }

    override fun onBackPressed() {
        Timber.e("onBackPressed()") // TODO: remove
        dismiss()
    }

    fun withArgs(args: TArgs) = also { it.args = args }
}
