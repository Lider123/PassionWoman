package ru.babaetskv.passionwoman.app.presentation.feature.home

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.SelectBrandEvent
import ru.babaetskv.passionwoman.app.analytics.event.SelectProductEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.GetHomeDataUseCase

class HomeViewModelImpl(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel<HomeViewModel.Router>(dependencies), HomeViewModel {
    override val homeItemsLiveData = MutableLiveData(emptyList<HomeItem>())

    init {
        loadData()
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        loadData()
    }

    override fun onHeaderPressed(header: HomeItem.Header) {
        when (header) {
            HEADER_PRODUCTS_SALE -> launch {
                navigateTo(
                    HomeViewModel.Router.ProductListScreen(
                    R.string.home_sale_products_title,
                    listOf(
                        Filter.DiscountOnly(stringProvider, true)
                    ),
                    Sorting.DEFAULT
                ))
            }
            HEADER_PRODUCTS_POPULAR -> launch {
                navigateTo(
                    HomeViewModel.Router.ProductListScreen(
                    R.string.home_popular_products_title,
                    listOf(),
                    Sorting.POPULARITY
                ))
            }
            HEADER_PRODUCTS_NEW -> launch {
                navigateTo(
                    HomeViewModel.Router.ProductListScreen(
                    R.string.home_new_products_title,
                    listOf(),
                    Sorting.NEW
                ))
            }
        }
    }

    override fun onPromotionPressed(promotion: Promotion) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    override fun onStoryPressed(story: Story) {
        val storiesItem = homeItemsLiveData.value?.find { it is HomeItem.Stories }
        with (storiesItem as? HomeItem.Stories) {
            val stories = this?.data ?: return

            val initialStoryIndex = stories.indexOfFirst { it.id == story.id }
            if (initialStoryIndex < 0) return

            launchWithLoading {
                navigateTo(HomeViewModel.Router.StoriesScreen(stories, initialStoryIndex))
            }
        }
    }

    override fun onBuyProductPressed(product: Product) {
        launch {
            navigateTo(HomeViewModel.Router.NewCartItemScreen(product))
        }
    }

    override fun onProductPressed(product: Product) {
        analyticsHandler.log(SelectProductEvent(product))
        launch {
            navigateTo(HomeViewModel.Router.ProductCardScreen(product))
        }
    }

    override fun onBrandPressed(brand: Brand) {
        analyticsHandler.log(SelectBrandEvent(brand))
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    private fun loadData() {
        launchWithLoading {
            val data = getHomeDataUseCase.execute()
            homeItemsLiveData.postValue(mutableListOf<HomeItem>().apply {
                if (data.promotions.isNotEmpty()) {
                    add(HomeItem.Promotions(data.promotions))
                }
                if (data.stories.isNotEmpty()) add(HomeItem.Stories(data.stories))
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

    companion object {
        private val HEADER_PRODUCTS_SALE = HomeItem.Header(R.string.home_sale_products_title, true)
        private val HEADER_PRODUCTS_POPULAR = HomeItem.Header(R.string.home_popular_products_title, true)
        private val HEADER_PRODUCTS_NEW = HomeItem.Header(R.string.home_new_products_title, true)
        private val HEADER_BRANDS = HomeItem.Header(R.string.home_popular_brands_title, false)
    }
}
