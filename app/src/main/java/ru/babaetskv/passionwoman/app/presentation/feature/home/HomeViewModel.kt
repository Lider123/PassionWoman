package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.GetHomeDataUseCase
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.utils.execute

class HomeViewModel(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val resources: Resources,
    notifier: Notifier,
    router: AppRouter
) : BaseViewModel(notifier, router) {
    val promotionsLiveData = MutableLiveData(emptyList<Promotion>())
    val saleProductsLiveData = MutableLiveData(emptyList<Product>())
    val popularProductsLiveData = MutableLiveData(emptyList<Product>())
    val newProductsLiveData = MutableLiveData(emptyList<Product>())
    val brandsLiveData = MutableLiveData(emptyList<Brand>())

    init {
        loadData()
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        loadData()
    }

    private fun loadData() {
        launchWithLoading {
            val data = getHomeDataUseCase.execute()
            promotionsLiveData.postValue(data.promotions)
            saleProductsLiveData.postValue(data.saleProducts)
            popularProductsLiveData.postValue(data.popularProducts)
            newProductsLiveData.postValue(data.newProducts)
            brandsLiveData.postValue(data.brands)
        }
    }

    fun onPromotionPressed(promotion: Promotion) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    fun onBuyProductPressed(product: Product) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    fun onProductPressed(product: Product) {
        router.navigateTo(Screens.productCard(product))
    }

    fun onPopularProductsPressed() {
        router.navigateTo(Screens.productList(
            resources.getString(R.string.home_popular_products_title),
            Filters.DEFAULT,
            Sorting.POPULARITY
        ))
    }

    fun onNewProductsPressed() {
        router.navigateTo(Screens.productList(
            resources.getString(R.string.home_new_products_title),
            Filters.DEFAULT,
            Sorting.NEW
        ))
    }

    fun onSaleProductsPressed() {
        router.navigateTo(Screens.productList(
            resources.getString(R.string.home_sale_products_title),
            Filters.DEFAULT.copy(
                discountOnly = true
            ),
            Sorting.DEFAULT
        ))
    }

    fun onBrandPressed(brand: Brand) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }
}
