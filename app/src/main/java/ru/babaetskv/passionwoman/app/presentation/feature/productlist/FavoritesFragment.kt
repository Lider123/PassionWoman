package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentProductListBinding
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.view.ToolbarView
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting

class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentComponent.NoArgs>() {
    private val binding: FragmentProductListBinding by viewBinding()
    private val productsAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(viewModel::onProductPressed, viewModel::onBuyPressed)
    }

    override val layoutRes: Int = R.layout.fragment_product_list
    override val viewModel: FavoritesViewModel by viewModel<FavoritesViewModelImpl>()
    override val screenName: String = ScreenKeys.FAVORITES

    override fun initViews() {
        super.initViews()
        binding.run {
            toolbar.run {
                title = resources.getString(R.string.product_list_favorites)
                setStartActions(
                    ToolbarView.Action(
                        iconRes = R.drawable.ic_back,
                        onClick = viewModel::onBackPressed
                    )
                )
            }
            layoutActions.isVisible = false
            rvProducts.adapter = productsAdapter
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.productsLiveData.observe(viewLifecycleOwner, ::populateProducts)
        viewModel.sortingLiveData.observe(viewLifecycleOwner, ::populateSorting)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is FavoritesViewModel.OpenLandscapeProductCardEvent -> {
                val fragment = ProductCardFragment.create(event.product.id, isSeparate = false)
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentDetailsContainer, fragment)
                    .commit()
            }
            else -> super.onEvent(event)
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

    companion object {

        fun create() = FavoritesFragment().withArgs(FragmentComponent.NoArgs)
    }
}
