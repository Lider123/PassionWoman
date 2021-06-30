package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentProductListBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Filters
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting

class ProductListFragment : BaseFragment<ProductListViewModel, ProductListViewModel.Router, ProductListFragment.Args>() {
    private val binding: FragmentProductListBinding by viewBinding()
    private val productsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(viewModel::onProductPressed, viewModel::onBuyPressed)
    }

    override val layoutRes: Int = R.layout.fragment_product_list
    override val viewModel: ProductListViewModel by viewModel { parametersOf(args) }

    override fun initViews() {
        super.initViews()
        binding.run {
            toolbar.run {
                title = args.title
                setOnStartClickListener {
                    viewModel.onBackPressed()
                }
            }
            layoutActions.isVisible = args.actionsAvailable
            btnFilters.setOnSingleClickListener {
                viewModel.onFiltersPressed()
            }
            btnSorting.setOnSingleClickListener {
                viewModel.onSortingPressed()
            }
            rvProducts.run {
                adapter = productsAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.productsLiveData.observe(viewLifecycleOwner, ::populateProducts)
        viewModel.sortingLiveData.observe(viewLifecycleOwner, ::populateSorting)
    }

    override fun handleRouterEvent(event: ProductListViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            is ProductListViewModel.Router.ProductCardScreen -> {
                router.navigateTo(Screens.productCard(event.product))
            }
            is ProductListViewModel.Router.SortingScreen -> {
                router.openBottomSheet(Screens.sorting(event.selectedSorting))
            }
        }
    }

    private fun populateSorting(sorting: Sorting) {
        binding.btnSorting.text = sorting.getUiName(viewModel.stringProvider)
    }

    private fun populateProducts(products: List<Product>) {
        productsAdapter.submitList(products) {
            binding.rvProducts.isVisible = products.isNotEmpty()
        }
    }

    @Parcelize
    data class Args(
        val categoryId: String?,
        val title: String,
        val filters: Filters,
        val sorting: Sorting,
        val actionsAvailable: Boolean
    ) : Parcelable

    companion object {

        fun create(categoryId: String?, title: String, filters: Filters, sorting: Sorting, actionsAvailable: Boolean) =
            ProductListFragment().withArgs(Args(
                categoryId = categoryId,
                title = title,
                filters = filters,
                sorting = sorting,
                actionsAvailable = actionsAvailable
            ))
    }
}
