package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentHomeBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.utils.bool

class HomeFragment : BaseFragment<HomeViewModel, HomeViewModel.Router, FragmentComponent.NoArgs>() {
    private val binding: FragmentHomeBinding by viewBinding()
    private val homeAdapter: ListDelegationAdapter<List<HomeItem>> by lazy {
        ListDelegationAdapter(
            headerHomeItemAdapterDelegate(viewModel::onHeaderPressed),
            promotionsHomeItemDelegate(viewModel::onPromotionPressed),
            storiesHomeItemDelegate(viewModel::onStoryPressed),
            productsHomeItemDelegate(viewModel::onProductPressed, viewModel::onBuyProductPressed),
            brandsHomeItemDelegate(viewModel::onBrandPressed)
        )
    }

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModel<HomeViewModelImpl>()
    override val applyBottomInset: Boolean = false
    override val screenName: String = ScreenKeys.HOME

    override fun initViews() {
        super.initViews()
        binding.rvHomeItems.run {
            adapter = homeAdapter
            addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_default))
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.homeItemsLiveData.observe(viewLifecycleOwner, ::populateHomeItems)
    }

    override fun handleRouterEvent(event: HomeViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            is HomeViewModel.Router.ProductCardScreen -> {
                if (requireContext().bool(R.bool.portrait_mode_only)) {
                    router.navigateTo(Screens.productCard(event.product.id))
                } else {
                    val detailsFragment = ProductCardFragment.create(event.product.id,
                        isSeparate = false
                    )
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentDetailsContainer, detailsFragment)
                        .commit()
                }
            }
            is HomeViewModel.Router.ProductListScreen -> {
                router.navigateTo(
                    Screens.productList(
                    getString(event.titleRes),
                    event.filters,
                    event.sorting
                ))
            }
            is HomeViewModel.Router.StoriesScreen -> {
                router.navigateTo(Screens.stories(event.stories, event.initialStoryIndex))
            }
            is HomeViewModel.Router.NewCartItemScreen -> {
                router.openBottomSheet(Screens.newCartItem(event.product))
            }
        }
    }

    private fun populateHomeItems(items: List<HomeItem>) {
        binding.rvHomeItems.isVisible = items.isNotEmpty()
        homeAdapter.run {
            this.items = items
            notifyDataSetChanged()
        }
    }

    companion object {

        fun create() = HomeFragment()
    }
}
