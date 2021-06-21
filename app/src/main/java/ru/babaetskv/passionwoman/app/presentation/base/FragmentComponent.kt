package ru.babaetskv.passionwoman.app.presentation.base

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.collect
import kotlinx.parcelize.Parcelize
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.view.ErrorView
import ru.babaetskv.passionwoman.app.presentation.view.LinearMockView
import ru.babaetskv.passionwoman.app.presentation.view.ProgressView
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException

interface FragmentComponent<VM, TRouterEvent: RouterEvent, TArgs : Parcelable> :
    BackButtonListener
    where VM : BaseViewModel<TRouterEvent> {
    val viewModel: VM
    val componentViewLifecycleOwner: LifecycleOwner
    val componentLifecycleScope: LifecycleCoroutineScope
    val componentView: View
    val componentContext: Context
    var componentArguments: Bundle
    var _args: TArgs?
    var args: TArgs
        get() = run {
            if (_args == null) {
                _args = componentArguments.getParcelable(ARGUMENTS_KEY)!!
            }
            _args!!
        }
        set(args) {
            componentArguments = bundleOf(ARGUMENTS_KEY to args)
            _args = args
        }

    fun initViews() = Unit

    @Suppress("UNCHECKED_CAST")
    fun initObservers() {
        viewModel.loadingLiveData.observe(componentViewLifecycleOwner, ::showLoading)
        viewModel.errorLiveData.observe(componentViewLifecycleOwner, ::showError)
        componentLifecycleScope.launchWhenResumed {
            viewModel.routerEventBus.collect {
                when (it) {
                    RouterEvent.GoBack -> onBackPressed()
                    else -> handleRouterEvent(it as TRouterEvent)
                }
            }
        }
    }

    fun handleRouterEvent(event: TRouterEvent) = Unit

    fun showError(exception: Exception?) {
        val errorView = componentView.findViewById<ErrorView>(R.id.errorView) ?: return

        exception ?: run {
            errorView.isVisible = false
            return
        }

        when (exception) {
            is NetworkDataException -> {
                errorView.isVisible = true
                errorView.message = exception.message ?: componentContext.getString(R.string.error_unknown)
                errorView.setBackButtonListener {
                    onBackPressed()
                }
                errorView.setActionButtonListener {
                    viewModel.onErrorActionPressed()
                }
            }
        }
    }

    fun showLoading(show: Boolean) {
        componentView.findViewById<LinearMockView>(R.id.mockView)?.isVisible = show
        componentView.findViewById<ProgressView>(R.id.progressView)?.isVisible = show
    }

    @Parcelize
    object NoArgs : Parcelable

    companion object {
        private const val ARGUMENTS_KEY = "FRAGMENT_ARGUMENTS"
    }
}
