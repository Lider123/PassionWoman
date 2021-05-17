package ru.babaetskv.passionwoman.app.presentation.feature.category

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentCategoryBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.Product

class CategoryFragment : BaseFragment<CategoryViewModel, CategoryFragment.Args>() {
    private val binding: FragmentCategoryBinding by viewBinding()
    private val productsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(viewModel::onProductPressed)
    }

    override val layoutRes: Int = R.layout.fragment_category
    override val viewModel: CategoryViewModel by viewModel { parametersOf(args) }

    override fun initViews() {
        super.initViews()
        binding.toolbar.setOnStartClickListener {
            viewModel.onBackPressed()
        }
        binding.rvProducts.run {
            adapter = productsAdapter
            addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.categoryLiveData.observe(viewLifecycleOwner, ::populateCategory)
        viewModel.productsLiveData.observe(viewLifecycleOwner, ::populateProducts)
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::showLoading)
    }

    private fun showLoading(show: Boolean) {
        binding.progress.isVisible = show
    }

    private fun populateCategory(category: Category) {
        binding.toolbar.title = category.name
    }

    private fun populateProducts(products: List<Product>) {
        productsAdapter.submitList(products) {
            binding.rvProducts.isVisible = products.isNotEmpty()
        }
    }

    @Parcelize
    data class Args(
        val category: Category
    ) : Parcelable

    companion object {

        fun create(category: Category) = CategoryFragment().withArgs(Args(category))
    }
}
