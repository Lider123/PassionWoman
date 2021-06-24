package ru.babaetskv.passionwoman.app.presentation.feature.home

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.GetHomeDataUseCase
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.utils.execute

class HomeViewModel(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    notifier: Notifier
) : BaseViewModel<HomeViewModel.Router>(notifier) {
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
        launch {
            navigateTo(Router.ProductCardScreen(product))
        }
    }

    fun onPopularProductsPressed() {
        launch {
            navigateTo(Router.ProductListScreen(
                R.string.home_popular_products_title,
                Filters.DEFAULT,
                Sorting.POPULARITY
            ))
        }
    }

    fun onNewProductsPressed() {
        launch {
            navigateTo(Router.ProductListScreen(
                R.string.home_new_products_title,
                Filters.DEFAULT,
                Sorting.NEW
            ))
        }
    }

    fun onSaleProductsPressed() {
       launch {
           navigateTo(Router.ProductListScreen(
               R.string.home_sale_products_title,
               Filters.DEFAULT.copy(
                   discountOnly = true
               ),
               Sorting.DEFAULT
           ))
       }
    }

    fun onBrandPressed(brand: Brand) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    sealed class Router : RouterEvent {

        data class ProductCardScreen(
            val product: Product
        ) : Router()

        data class ProductListScreen(
            @StringRes val titleRes: Int,
            val filters: Filters,
            val sorting: Sorting
        ) : Router()
    }
}
