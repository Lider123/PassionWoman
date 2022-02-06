package ru.babaetskv.passionwoman.app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.presentation.feature.demopresets.DemoPresetsViewModelImpl

val demoModule = module {
    viewModel { DemoPresetsViewModelImpl(get(), get(), get(), get(), get(), get()) }
}
