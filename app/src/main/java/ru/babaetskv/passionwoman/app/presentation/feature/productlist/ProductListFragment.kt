package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentProductListBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.domain.model.Filters
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting

class ProductListFragment : BaseFragment<ProductListViewModel, ProductListFragment.Args>() {
    private val binding: FragmentProductListBinding by viewBinding()
    private val productsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(viewModel::onProductPressed, viewModel::onBuyPressed)
    }

    override val layoutRes: Int = R.layout.fragment_product_list
    override val viewModel: ProductListViewModel by viewModel { parametersOf(args) }

    override fun initViews() {
        super.initViews()
        binding.toolbar.run {
            title = args.title
            setOnStartClickListener {
                viewModel.onBackPressed()
            }
        }
        binding.rvProducts.run {
            adapter = productsAdapter
            addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.productsLiveData.observe(viewLifecycleOwner, ::populateProducts)
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
        val sorting: Sorting
    ) : Parcelable

    companion object {

        fun create(categoryId: String?, title: String, filters: Filters, sorting: Sorting) =
            ProductListFragment().withArgs(Args(
                categoryId = categoryId,
                title = title,
                filters = filters,
                sorting = sorting
            ))
    }
}
