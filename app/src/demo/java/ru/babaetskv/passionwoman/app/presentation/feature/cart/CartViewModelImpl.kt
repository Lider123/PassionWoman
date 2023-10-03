package ru.babaetskv.passionwoman.app.presentation.feature.cart

import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.CheckoutUseCase
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.StartOrderStatusUpdateServiceUseCase

class CartViewModelImpl(
    addToCartUseCase: AddToCartUseCase,
    removeFromCartUseCase: RemoveFromCartUseCase,
    private val startOrderStatusUpdateServiceUseCase: StartOrderStatusUpdateServiceUseCase,
    cartFlow: CartFlow,
    checkoutUseCase: CheckoutUseCase,
    stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseCartViewModelImpl(addToCartUseCase, removeFromCartUseCase, cartFlow, checkoutUseCase, stringProvider, dependencies) {

    override suspend fun checkout(): Long =
        super.checkout()
            .also {
                startOrderStatusUpdateServiceUseCase.execute(it)
            }
}
