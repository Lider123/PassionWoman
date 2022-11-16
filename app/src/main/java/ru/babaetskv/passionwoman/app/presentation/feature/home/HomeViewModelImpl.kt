package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Screen
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.SelectBrandEvent
import ru.babaetskv.passionwoman.app.analytics.event.SelectProductEvent
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.permission.Permission
import ru.babaetskv.passionwoman.app.permission.PermissionManager
import ru.babaetskv.passionwoman.app.permission.PermissionStatus
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.GetHomeDataUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute
import java.lang.Exception

class HomeViewModelImpl(
    private val permissionManager: PermissionManager,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), HomeViewModel {
    override val homeItemsLiveData = MutableLiveData(emptyList<HomeItem>())
    override val pushPermissionStatusLiveData = MutableLiveData<PermissionStatus>()

    init {
        loadData()
    }

    override fun onStart(screenName: String) {
        super.onStart(screenName)
        if (Build.VERSION.SDK_INT >= 33) checkPushPermission()
    }

    override fun onErrorActionPressed(exception: Exception) {
        when (exception) {
            is GetHomeDataUseCase.GetHomeDataException -> loadData()
            else -> super.onErrorActionPressed(exception)
        }
    }

    override fun onHeaderPressed(header: HomeItem.Header) {
        val screen: Screen? = when (header) {
            HEADER_PRODUCTS_SALE -> {
                Screens.productList(
                    R.string.home_sale_products_title,
                    listOf(
                        Filter.DiscountOnly(stringProvider, true)
                    ),
                    Sorting.DEFAULT
                )
            }
            HEADER_PRODUCTS_POPULAR -> {
                Screens.productList(
                    R.string.home_popular_products_title,
                    listOf(),
                    Sorting.POPULARITY
                )
            }
            HEADER_PRODUCTS_NEW -> {
                Screens.productList(
                    R.string.home_new_products_title,
                    listOf(),
                    Sorting.NEW
                )
            }
            else -> null
        }
        screen?.let(router::navigateTo)
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

            router.navigateTo(Screens.stories(stories, initialStoryIndex))
        }
    }

    override fun onBuyProductPressed(product: Product) {
        router.openBottomSheet(Screens.newCartItem(product))
    }

    override fun onProductPressed(product: Product) {
        analyticsHandler.log(SelectProductEvent(product))
        if (isPortraitModeOnly) {
            router.navigateTo(Screens.productCard(product.id))
        } else launch {
            sendEvent(HomeViewModel.OpenLandscapeProductCardEvent(product))
        }
    }

    override fun onBrandPressed(brand: Brand) {
        analyticsHandler.log(SelectBrandEvent(brand))
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    override fun onPushRationaleDialogConfirm() {
        pushPermissionStatusLiveData.postValue(PermissionStatus.REQUEST_PERMISSION)
    }

    override fun onPushRationaleDialogReject() {
        pushPermissionStatusLiveData.postValue(PermissionStatus.DENIED)
    }

    @RequiresApi(33)
    override fun onPushPermissionRequestResult(isGranted: Boolean) {
        if (!isGranted) {
            notifier.newRequest(this, R.string.home_push_permission_not_granted_error)
                .sendError()
        }
        pushPermissionStatusLiveData.postValue(
            if (isGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED
        )
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

    @RequiresApi(33)
    private fun checkPushPermission() {
        val status = permissionManager.getStatus(Permission.PUSH_NOTIFICATION)
        pushPermissionStatusLiveData.postValue(status)
    }

    companion object {
        private val HEADER_PRODUCTS_SALE = HomeItem.Header(R.string.home_sale_products_title, true)
        private val HEADER_PRODUCTS_POPULAR = HomeItem.Header(R.string.home_popular_products_title, true)
        private val HEADER_PRODUCTS_NEW = HomeItem.Header(R.string.home_new_products_title, true)
        private val HEADER_BRANDS = HomeItem.Header(R.string.home_popular_brands_title, false)
    }
}
