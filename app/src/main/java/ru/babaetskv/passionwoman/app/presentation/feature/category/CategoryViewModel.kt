package ru.babaetskv.passionwoman.app.presentation.feature.category

import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.GetProductsUseCase
import ru.babaetskv.passionwoman.domain.model.Product

class CategoryViewModel(
    args: CategoryFragment.Args,
    private val getProductsUseCase: GetProductsUseCase,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {
    val categoryLiveData = MutableLiveData(args.category)
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
            val products = getProductsUseCase.execute(categoryLiveData.value!!.id)
            productsLiveData.postValue(products)
        }
    }

    fun onProductPressed(product: Product) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendError()
    }

    fun onBuyPressed(product: Product) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendError()
    }
}
