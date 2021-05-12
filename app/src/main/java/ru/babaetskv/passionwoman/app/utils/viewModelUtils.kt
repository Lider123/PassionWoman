package ru.babaetskv.passionwoman.app.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

inline fun <reified T : ViewModel> Kodein.Builder.viewModel(overrides: Boolean? = null): Kodein.Builder.TypeBinder<T> =
    bind<T>(T::class.java.simpleName, overrides)

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : KodeinAware, T : Fragment =
    lazy { ViewModelProviders.of(this, direct.instance()).get(VM::class.java) }
