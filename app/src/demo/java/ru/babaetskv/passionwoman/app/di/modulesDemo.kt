package ru.babaetskv.passionwoman.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.presentation.feature.demopresets.DemoPresetsFragment
import ru.babaetskv.passionwoman.app.presentation.feature.demopresets.DemoPresetsViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.interactor.InitDatabaseInteractor
import ru.babaetskv.passionwoman.data.database.DatabaseProvider
import ru.babaetskv.passionwoman.data.repository.ProductsRepositoryImpl
import ru.babaetskv.passionwoman.domain.repository.ProductsRepository
import ru.babaetskv.passionwoman.domain.usecase.InitDatabaseUseCase

val demoModule = module {
    single { DatabaseProvider.provideDatabase(androidContext(), get()) }

    viewModel { (args: DemoPresetsFragment.Args) ->
        DemoPresetsViewModelImpl(args, get(), get(), get(), get(), get(), get())
    }

    factory<InitDatabaseUseCase> { InitDatabaseInteractor(get(), get(), get(), get()) }

    single<ProductsRepository> { ProductsRepositoryImpl(get()) }
}
