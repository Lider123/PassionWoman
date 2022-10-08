package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentProductListBinding
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.view.ToolbarView
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

// TODO: fix actions view
class ProductListFragment : BaseFragment<ProductListViewModel, ProductListFragment.Args>() {
    private val binding: FragmentProductListBinding by viewBinding()
    private val productsAdapter: PagedProductsAdapter by lazy {
        PagedProductsAdapter(viewModel::onProductPressed, viewModel::onBuyPressed).apply {
            addLoadStateListener(viewModel::onLoadStateChanged)
        }
    }

    override val layoutRes: Int = R.layout.fragment_product_list
    override val viewModel: ProductListViewModel by viewModel<ProductListViewModelImpl> {
        parametersOf(args)
    }
    override val screenName: String = ScreenKeys.PRODUCT_LIST

    override fun initViews() {
        super.initViews()
        binding.run {
            toolbar.setStartActions(
                ToolbarView.Action(
                    iconRes = R.drawable.ic_back,
                    onClick = viewModel::onBackPressed
                )
            )
            layoutActions.isVisible = args.actionsAvailable
            btnFilters.setOnSingleClickListener {
                viewModel.onFiltersPressed()
            }
            btnSorting.setOnSingleClickListener {
                viewModel.onSortingPressed()
            }
            rvProducts.adapter = productsAdapter
            etSearch.doAfterTextChanged {
                viewModel.onSearchQueryChanged(it.toString())
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        lifecycleScope.launchWhenResumed {
            viewModel.productsFlow.collect(::populateProducts)
        }
        viewModel.modeLiveData.observe(viewLifecycleOwner, ::populateMode)
        viewModel.sortingLiveData.observe(viewLifecycleOwner, ::populateSorting)
        viewModel.appliedFiltersCountLiveData.observe(viewLifecycleOwner, ::populateFiltersBadge)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is ProductListViewModel.OpenLandscapeProductCard -> {
                val fragment = ProductCardFragment.create(event.product.id, isSeparate = false)
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentDetailsContainer, fragment)
                    .commit()
            }
            else -> super.onEvent(event)
        }
    }

    private fun populateMode(mode: ProductListMode) {
        binding.run {
            when (mode) {
                is ProductListMode.Category -> {
                    toolbar.title = mode.category.name
                    layoutSearch.isVisible = false
                }
                is ProductListMode.Specific -> {
                    toolbar.title = getString(mode.titleRes)
                    layoutSearch.isVisible = false
                }
                is ProductListMode.Search -> {
                    toolbar.title = ""
                    layoutSearch.isVisible = true
                }
            }
        }
    }

    private fun populateFiltersBadge(appliedFiltersCount: Int) {
        binding.tvFiltersCount.run {
            isVisible = appliedFiltersCount > 0
            text = appliedFiltersCount.toString()
        }
    }

    private fun populateSorting(sorting: Sorting) {
        binding.btnSorting.text = sorting.getUiName(viewModel.stringProvider)
    }

    private suspend fun populateProducts(products: PagingData<Product>) {
        // TODO: fix animations on data submit
        productsAdapter.submitData(products)
    }

    @Parcelize
    data class Args(
        val mode: ProductListMode,
        val filters: List<Filter>,
        val sorting: Sorting,
        val actionsAvailable: Boolean
    ) : Parcelable

    companion object {

        fun create(
            mode: ProductListMode,
            filters: List<Filter>,
            sorting: Sorting,
            actionsAvailable: Boolean
        ) = ProductListFragment().withArgs(Args(
            mode = mode,
            filters = filters,
            sorting = sorting,
            actionsAvailable = actionsAvailable
        ))
    }
}
