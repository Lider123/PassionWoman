package ru.babaetskv.passionwoman.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.presentation.feature.cart.CartViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.demopresets.DemoPresetsFragment
import ru.babaetskv.passionwoman.app.presentation.feature.demopresets.DemoPresetsViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.interactor.InitDatabaseInteractor
import ru.babaetskv.passionwoman.app.presentation.interactor.StartOrderStatusUpdateServiceInteractor
import ru.babaetskv.passionwoman.app.presentation.interactor.UpdateOrderStatusInteractor
import ru.babaetskv.passionwoman.data.database.DatabaseProvider
import ru.babaetskv.passionwoman.data.repository.OrdersRepositoryImpl
import ru.babaetskv.passionwoman.data.repository.ProductsRepositoryImpl
import ru.babaetskv.passionwoman.data.repository.OrdersRepository
import ru.babaetskv.passionwoman.data.repository.ProductsRepository
import ru.babaetskv.passionwoman.domain.usecase.InitDatabaseUseCase
import ru.babaetskv.passionwoman.domain.usecase.StartOrderStatusUpdateServiceUseCase
import ru.babaetskv.passionwoman.domain.usecase.UpdateOrderStatusUseCase

val demoModule = module {
    single { DatabaseProvider.provideDatabase(androidContext(), get()) }

    viewModel { (args: DemoPresetsFragment.Args) ->
        DemoPresetsViewModelImpl(args, get(), get(), get(), get(), get(), get(), get(), get())
    }
    viewModel {
        CartViewModelImpl(get(), get(), get(), get(), get(), get(), get())
    }

    factory<InitDatabaseUseCase> { InitDatabaseInteractor(get(), get(), get(), get()) }
    factory<StartOrderStatusUpdateServiceUseCase> { StartOrderStatusUpdateServiceInteractor(androidContext(), get()) }
    factory<UpdateOrderStatusUseCase> { UpdateOrderStatusInteractor(get(), get()) }

    single<ProductsRepository> { ProductsRepositoryImpl(get()) }
    single<OrdersRepository> { OrdersRepositoryImpl(get()) }
}
