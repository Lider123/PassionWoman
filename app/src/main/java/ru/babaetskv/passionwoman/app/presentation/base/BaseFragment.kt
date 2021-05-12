package ru.babaetskv.passionwoman.app.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import ru.babaetskv.passionwoman.app.MainApp

abstract class BaseFragment<VM : BaseViewModel> : Fragment(), KodeinAware {
    abstract val layoutRes: Int
    abstract val viewModel: VM

    override val kodein: Kodein by lazy {
        MainApp.instance.kodein
    }

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
}
