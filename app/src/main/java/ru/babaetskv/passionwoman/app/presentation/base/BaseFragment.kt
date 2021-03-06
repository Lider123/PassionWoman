package ru.babaetskv.passionwoman.app.presentation.base

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.utils.setInsetsListener

abstract class BaseFragment<VM : IViewModel, TRouterEvent: RouterEvent, TArgs : Parcelable> :
    Fragment(),
    FragmentComponent<VM, TRouterEvent, TArgs> {
    protected val router: AppRouter by inject()
    protected open val applyTopInset: Boolean = true
    protected open val applyBottomInset: Boolean = true

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
    override val componentLifecycleScope: LifecycleCoroutineScope
        get() = lifecycleScope
    override val componentViewLifecycleOwner: LifecycleOwner
        get() = viewLifecycleOwner

    abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.contentInsetsView)
            ?.setInsetsListener(
                top = applyTopInset,
                bottom = applyBottomInset
            )
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

    override fun initObservers() {
        super.initObservers()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.eventHub.flow.collect(::onEvent)
        }
    }

    override fun onBackPressed() {
        router.exit()
    }

    protected open fun onEvent(event: InnerEvent) = Unit

    fun withArgs(args: TArgs) = also { it.args = args }
}
