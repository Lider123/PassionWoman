package ru.babaetskv.passionwoman.app.presentation.base

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.presentation.view.StubView
import ru.babaetskv.passionwoman.app.presentation.view.ProgressView
import ru.babaetskv.passionwoman.domain.exceptions.GatewayException
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import timber.log.Timber

interface ViewComponent<VM : IViewModel> {
    val viewModel: VM
    val screenName: String
    val componentView: View
    val componentViewLifecycleOwner: LifecycleOwner
    val componentContext: Context

    fun onBackPressed()

    fun onEvent(event: Event) = Unit

    fun initViews() = Unit

    fun initObservers() {
        Timber.e("initObservers()")
        viewModel.loadingLiveData.observe(componentViewLifecycleOwner, ::showLoading)
        viewModel.errorLiveData.observe(componentViewLifecycleOwner, ::showError)
        componentViewLifecycleOwner.lifecycleScope.launchWhenResumed {
            Timber.e("launchWhenResumed()")
            viewModel.eventFlow.collect(::onEvent)
        }
    }

    fun showLoading(show: Boolean) {
        componentView.findViewById<View>(R.id.mockView)?.isVisible = show
        componentView.findViewById<ProgressView>(R.id.progressView)?.isVisible = show
    }

    fun showError(exception: Exception?) {
        val errorView = componentView.findViewById<StubView>(R.id.errorView) ?: return

        exception ?: run {
            errorView.isVisible = false
            return
        }

        errorView.isVisible = true
        when (exception) {
            is UseCaseException.Data -> {
                errorView.message = exception.message
                errorView.setBackButtonListener {
                    onBackPressed()
                }
                errorView.isActionButtonVisible = true
                errorView.setActionButtonListener {
                    viewModel.onErrorActionPressed(exception)
                }
            }
            is UseCaseException.EmptyData -> {
                errorView.message = exception.message
                errorView.setBackButtonListener {
                    onBackPressed()
                }
                errorView.isActionButtonVisible = false
            }
            is GatewayException.Unauthorized -> {
                errorView.message = exception.message
                errorView.setBackButtonListener {
                    onBackPressed()
                }
                errorView.isActionButtonVisible = true
                errorView.setActionButtonListener {
                    viewModel.onErrorActionPressed(exception)
                }
                errorView.action = componentContext.getString(R.string.log_in)
            }
        }
    }
}
