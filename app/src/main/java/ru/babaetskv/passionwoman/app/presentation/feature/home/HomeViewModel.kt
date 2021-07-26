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
    val homeItemsLiveData = MutableLiveData(emptyList<HomeItem>())

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
            homeItemsLiveData.postValue(mutableListOf<HomeItem>().apply {
                if (data.promotions.isNotEmpty()) add(HomeItem.Promotions(data.promotions))
                if (data.saleProducts.isNotEmpty()) {
                    add(HEADER_PRODUCTS_SALE)
                    add(HomeItem.Products(data.saleProducts))
                }
                if (data.popularProducts.isNotEmpty()) {
                    add(HEADER_PRODUCTS_POPULAR)
                    add(HomeItem.Products(data.popularProducts))
                }
                if (data.newProducts.isNotEmpty()) {
                    add(HEADER_PRODUCTS_NEW)
                    add(HomeItem.Products(data.newProducts))
                }
                if (data.brands.isNotEmpty()) {
                    add(HEADER_BRANDS)
                    add(HomeItem.Brands(data.brands))
                }
            })
        }
    }

    fun onHeaderPressed(header: HomeItem.Header) {
        when (header) {
            HEADER_PRODUCTS_SALE -> launch {
                navigateTo(Router.ProductListScreen(
                    R.string.home_sale_products_title,
                    Filters.DEFAULT.copy(
                        discountOnly = true
                    ),
                    Sorting.DEFAULT
                ))
            }
            HEADER_PRODUCTS_POPULAR -> launch {
                navigateTo(Router.ProductListScreen(
                    R.string.home_popular_products_title,
                    Filters.DEFAULT,
                    Sorting.POPULARITY
                ))
            }
            HEADER_PRODUCTS_NEW -> launch {
                navigateTo(Router.ProductListScreen(
                    R.string.home_new_products_title,
                    Filters.DEFAULT,
                    Sorting.NEW
                ))
            }
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

    companion object {
        private val HEADER_PRODUCTS_SALE = HomeItem.Header(R.string.home_sale_products_title, true)
        private val HEADER_PRODUCTS_POPULAR = HomeItem.Header(R.string.home_popular_products_title, true)
        private val HEADER_PRODUCTS_NEW = HomeItem.Header(R.string.home_new_products_title, true)
        private val HEADER_BRANDS = HomeItem.Header(R.string.home_popular_brands_title, false)
    }
}
