package ru.babaetskv.passionwoman.app.presentation.base

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.parcelize.Parcelize
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.view.ErrorView
import ru.babaetskv.passionwoman.app.presentation.view.LinearMockView
import ru.babaetskv.passionwoman.app.presentation.view.ProgressView
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException

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

    open fun onBackPressed() = viewModel.onBackPressed()

    open fun initViews() = Unit

    open fun initObservers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::showLoading)
        viewModel.errorLiveData.observe(viewLifecycleOwner, ::showError)
    }

    open fun showError(exception: Exception?) {
        val errorView = requireView().findViewById<ErrorView>(R.id.errorView) ?: return

        exception ?: run {
            errorView.isVisible = false
            return
        }

        when (exception) {
            is NetworkDataException -> {
                errorView.isVisible = true
                errorView.message = exception.message ?: getString(R.string.error_unknown)
                errorView.setBackButtonListener {
                    viewModel.onBackPressed()
                }
                errorView.setActionButtonListener {
                    viewModel.onErrorActionPressed()
                }
            }
        }
    }

    open fun showLoading(show: Boolean) {
        requireView().findViewById<LinearMockView>(R.id.mockView)?.isVisible = show
        requireView().findViewById<ProgressView>(R.id.progressView)?.isVisible = show
    }

    fun withArgs(args: TArgs) = also { it.args = args }

    @Parcelize
    object NoArgs : Parcelable

    companion object {
        private const val ARGUMENTS_KEY = "FRAGMENT_ARGUMENTS"
    }
}
