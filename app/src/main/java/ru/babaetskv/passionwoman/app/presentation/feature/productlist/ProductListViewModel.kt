package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.GetProductsUseCase
import ru.babaetskv.passionwoman.domain.model.Product

class ProductListViewModel(
    private val args: ProductListFragment.Args,
    private val getProductsUseCase: GetProductsUseCase,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {
    private val filters = args.filters
    private val sorting = args.sorting

    val productsLiveData = MutableLiveData<List<Product>>(emptyList())

    init {
        loadProducts()
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        loadProducts()
    }

    private fun loadProducts() {
        launchWithLoading {
            // TODO: add pagination
            val products = getProductsUseCase.execute(GetProductsUseCase.Params(
                categoryId = args.categoryId,
                filters = filters,
                sorting = sorting,
                limit = 10,
                offset = 0
            ))
            // TODO: handle empty products list
            productsLiveData.postValue(products)
        }
    }

    fun onProductPressed(product: Product) {
        router.navigateTo(Screens.productCard(product))
    }

    fun onBuyPressed(product: Product) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendError()
    }
}
