package ru.babaetskv.passionwoman.app.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.parcelize.Parcelize
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.view.ErrorView
import ru.babaetskv.passionwoman.app.presentation.view.LinearMockView
import ru.babaetskv.passionwoman.app.presentation.view.ProgressView
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException

abstract class BaseBottomSheetDialogFragment<VM, TRouterEvent: RouterEvent, TArgs : Parcelable> : BottomSheetDialogFragment(), BackButtonListener where VM : BaseViewModel<TRouterEvent> {
    private var _args: TArgs? = null

    protected val router: AppRouter by inject()

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

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        dismiss()
    }

    open fun initViews() = Unit

    @Suppress("UNCHECKED_CAST")
    open fun initObservers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::showLoading)
        viewModel.errorLiveData.observe(viewLifecycleOwner, ::showError)
        lifecycleScope.launchWhenResumed {
            viewModel.routerEventBus.collect {
                when (it) {
                    RouterEvent.GoBack -> onBackPressed()
                    else -> handleRouterEvent(it as TRouterEvent)
                }
            }
        }
    }

    open fun handleRouterEvent(event: TRouterEvent) = Unit

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
                    onBackPressed()
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
