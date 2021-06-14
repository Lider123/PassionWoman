package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentHomeBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductsAdapter
import ru.babaetskv.passionwoman.domain.model.Brand
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Promotion

class HomeFragment : BaseFragment<HomeViewModel, BaseFragment.NoArgs>() {
    private val binding: FragmentHomeBinding by viewBinding()
    private val promotionsAdapter: PromotionsAdapter by lazy {
        PromotionsAdapter(viewModel::onPromotionPressed)
    }
    private val saleProductsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(viewModel::onProductPressed, viewModel::onBuyProductPressed,
            itemWidthRatio = PRODUCT_ITEM_WIDTH_RATIO
        )
    }
    private val popularProductsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(viewModel::onProductPressed, viewModel::onBuyProductPressed,
            itemWidthRatio = PRODUCT_ITEM_WIDTH_RATIO
        )
    }
    private val newProductsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(viewModel::onProductPressed, viewModel::onBuyProductPressed,
            itemWidthRatio = PRODUCT_ITEM_WIDTH_RATIO
        )
    }
    private val brandsAdapter: BrandsAdapter by lazy {
        BrandsAdapter(viewModel::onBrandPressed)
    }

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModel()

    override fun initViews() {
        super.initViews()
        binding.run {
            vpPromotions.run {
                adapter = promotionsAdapter.apply {
                    registerAdapterDataObserver(pageIndicatorPromotions.adapterDataObserver)
                }
                pageIndicatorPromotions.setViewPager(this)
            }
            rvSaleProducts.run {
                adapter = saleProductsAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_default))
            }
            rvPopularProducts.run {
                adapter = popularProductsAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_default))
            }
            rvNewProducts.run {
                adapter = newProductsAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_default))
            }
            rvBrands.run {
                adapter = brandsAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
            }
            tvSaleProductsTitle.setOnClickListener {
                viewModel.onSaleProductsPressed()
            }
            tvPopularProductsTitle.setOnClickListener {
                viewModel.onPopularProductsPressed()
            }
            tvNewProductsTitle.setOnClickListener {
                viewModel.onNewProductsPressed()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.promotionsLiveData.observe(viewLifecycleOwner, ::populatePromotions)
        viewModel.saleProductsLiveData.observe(viewLifecycleOwner, ::populateSaleProducts)
        viewModel.popularProductsLiveData.observe(viewLifecycleOwner, ::populatePopularProducts)
        viewModel.newProductsLiveData.observe(viewLifecycleOwner, ::populateNewProducts)
        viewModel.brandsLiveData.observe(viewLifecycleOwner, ::populateBrands)
    }

    private fun populateBrands(brands: List<Brand>) {
        brandsAdapter.submitList(brands) {
            binding.layoutBrands.isVisible = brands.isNotEmpty()
        }
    }

    private fun populatePromotions(promotions: List<Promotion>) {
        promotionsAdapter.submitList(promotions) {
            binding.layoutPromotions.isVisible = promotions.isNotEmpty()
        }
    }

    private fun populateSaleProducts(products: List<Product>) {
        saleProductsAdapter.submitList(products) {
            binding.layoutSaleProducts.isVisible = products.isNotEmpty()
        }
    }

    private fun populatePopularProducts(products: List<Product>) {
        popularProductsAdapter.submitList(products) {
            binding.layoutPopularProducts.isVisible = products.isNotEmpty()
        }
    }

    private fun populateNewProducts(products: List<Product>) {
        newProductsAdapter.submitList(products) {
            binding.layoutNewProducts.isVisible = products.isNotEmpty()
        }
    }

    companion object {
        private const val PRODUCT_ITEM_WIDTH_RATIO = 0.41f

        fun create() = HomeFragment()
    }
}
