package ru.babaetskv.passionwoman.app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.presentation.feature.demopresets.DemoPresetsViewModel

val demoModule = module {
    viewModel { DemoPresetsViewModel(get(), get(), get(), get(), get(), get()) }
}
