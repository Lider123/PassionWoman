package ru.babaetskv.passionwoman.app.presentation.feature.cart

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.model.*

class CartViewModelImpl(
    dependencies: ViewModelDependencies
) : BaseViewModel<CartViewModel.Router>(dependencies), CartViewModel {
    override val cartItemsLiveData = MutableLiveData(listOf( // TODO: set real data
        CartItem(
            product = Product(
                id = "product_panties_2",
                category = Category(
                    id = "",
                    name = "",
                    image = Image("")
                ),
                name = "Panties string Lui et Elle Resille",
                description = "",
                preview = Image("file:///android_asset/image/product_panties_2_preview.jpg"),
                price = Price(35.70f),
                priceWithDiscount = Price(2.69f),
                rating = 0f,
                brand = Brand(
                    id = "",
                    name = "",
                    logo = Image("")
                ),
                colors = emptyList()
            ),
            selectedColor = Color(
                code = "",
                uiName = "",
                hex = "#ff0000"
            ),
            selectedSize = ProductSize(
                value = "S",
                isAvailable = true
            ),
            count = 2
        )
    ))

    override fun onAddCartItemPressed(item: CartItem) {
        // TODO: remove
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    override fun onRemoveCartItemPressed(item: CartItem) {
        // TODO: remove
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    override fun onCheckoutPressed() {
        // TODO: remove
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }
}
