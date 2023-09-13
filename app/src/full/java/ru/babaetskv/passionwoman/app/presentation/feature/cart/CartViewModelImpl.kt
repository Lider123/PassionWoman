package ru.babaetskv.passionwoman.app.presentation.feature.cart

import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.CheckoutUseCase
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromCartUseCase

class CartViewModelImpl(
    addToCartUseCase: AddToCartUseCase,
    removeFromCartUseCase: RemoveFromCartUseCase,
    cartFlow: CartFlow,
    checkoutUseCase: CheckoutUseCase,
    stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseCartViewModelImpl(addToCartUseCase, removeFromCartUseCase, cartFlow, checkoutUseCase, stringProvider, dependencies)
