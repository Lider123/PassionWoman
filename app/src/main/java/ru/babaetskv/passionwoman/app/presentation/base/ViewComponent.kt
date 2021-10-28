package ru.babaetskv.passionwoman.app.presentation.base

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.collect
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.view.StubView
import ru.babaetskv.passionwoman.app.presentation.view.LinearMockView
import ru.babaetskv.passionwoman.app.presentation.view.ProgressView
import ru.babaetskv.passionwoman.domain.interactor.exception.EmptyDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException

interface ViewComponent<VM, TRouterEvent : RouterEvent> where VM : BaseViewModel<TRouterEvent> {
    val viewModel: VM
    val screenName: String
    val componentView: View
    val componentViewLifecycleOwner: LifecycleOwner
    val componentLifecycleScope: LifecycleCoroutineScope
    val componentContext: Context

    fun onBackPressed()

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

    fun showLoading(show: Boolean) {
        componentView.findViewById<LinearMockView>(R.id.mockView)?.isVisible = show
        componentView.findViewById<ProgressView>(R.id.progressView)?.isVisible = show
    }

    fun showError(exception: Exception?) {
        val emptyView = componentView.findViewById<StubView>(R.id.emptyView) ?: return

        exception ?: run {
            emptyView.isVisible = false
            return
        }

        when (exception) {
            is NetworkDataException -> {
                emptyView.isVisible = true
                emptyView.message = exception.message ?: componentContext.getString(R.string.error_unknown)
                emptyView.setBackButtonListener {
                    onBackPressed()
                }
                emptyView.setActionButtonListener {
                    viewModel.onErrorActionPressed()
                }
            }
            is EmptyDataException -> {
                emptyView.isVisible = true
                emptyView.message = exception.message ?: componentContext.getString(R.string.error_no_data)
                emptyView.setBackButtonListener {
                    onBackPressed()
                }
                emptyView.isActionButtonVisible = false
            }
        }
    }
}
