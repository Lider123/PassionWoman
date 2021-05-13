package ru.babaetskv.passionwoman.app.presentation.base

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.parcel.Parcelize

abstract class BaseFragment<VM : BaseViewModel, TArgs : Parcelable> : Fragment() {
    private var _args: TArgs? = null

    var args: TArgs
        get() = run {
            if (_args == null) {
                _args = requireArguments().getParcelable(ARGUMENTS_KEY)!!
            }
            _args!!
        }
        set(args) {
            arguments = bundleOf(ARGUMENTS_KEY to args)
            _args = args
        }


    abstract val layoutRes: Int
    abstract val viewModel: VM

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

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    open fun initViews() = Unit

    open fun initObservers() = Unit

    fun withArgs(args: TArgs) = also { it.args = args }

    @Parcelize
    object NoArgs : Parcelable

    companion object {
        private const val ARGUMENTS_KEY = "FRAGMENT_ARGUMENTS"
    }
}
