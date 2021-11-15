package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentProductCardBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setHtmlText
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.toPriceString
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductSize

class ProductCardFragment : BaseFragment<ProductCardViewModel, ProductCardViewModel.Router, ProductCardFragment.Args>() {
    private val binding: FragmentProductCardBinding by viewBinding()
    private val productPhotosAdapter: ProductPhotosAdapter by lazy {
        ProductPhotosAdapter()
    }
    private val productSizesAdapter: ProductSizesAdapter by lazy {
        ProductSizesAdapter(viewModel::onSizePressed)
    }
    private val productColorsAdapter: ProductColorsAdapter by lazy {
        ProductColorsAdapter(viewModel::onColorItemPressed)
    }

    override val layoutRes: Int = R.layout.fragment_product_card
    override val viewModel: ProductCardViewModel by viewModel {
        parametersOf(args)
    }
    override val screenName: String = ScreenKeys.PRODUCT_CARD

    override fun initViews() {
        super.initViews()
        binding.run {
            toolbar.run {
                setOnStartClickListener {
                    viewModel.onBackPressed()
                }
                setOnEndClickListener {
                    viewModel.onFavoritePressed()
                }
            }
            vpPhotos.run {
                adapter = productPhotosAdapter.apply {
                    registerAdapterDataObserver(pageIndicator.adapterDataObserver)
                }
                pageIndicator.setViewPager(this)
            }
            rvColors.run {
                adapter = productColorsAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
            }
            rvSizes.run {
                adapter = productSizesAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_extra_small))
            }
            btnAddToCart.setOnSingleClickListener {
                viewModel.onAddToCartPressed()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.productLiveData.observe(viewLifecycleOwner, ::populateProduct)
        viewModel.productColorsLiveData.observe(viewLifecycleOwner, ::populateProductColorItems)
        viewModel.productPhotosLiveData.observe(viewLifecycleOwner, ::populateProductPhotos)
        viewModel.productSizesLiveData.observe(viewLifecycleOwner, ::populateProductSizes)
        viewModel.isFavoriteLiveData.observe(viewLifecycleOwner, ::populateFavorite)
    }

    private fun populateFavorite(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.ic_like_checked else R.drawable.ic_like_unchecked
        binding.toolbar.setActionEnd(iconRes)
    }

    private fun populateProductPhotos(photos: List<Image>) {
        productPhotosAdapter.submitList(photos) {
            binding.layoutEmpty.isVisible = photos.isEmpty()
        }
    }

    private fun populateProduct(product: Product) {
        binding.run {
            tvProductName.text = product.name
            ratingBar.rating = product.rating
            if (product.discountRate > 0) {
                tvPrice.text = product.priceWithDiscount.toPriceString()
                tvPriceDeleted.run {
                    isVisible = true
                    setHtmlText(getString(R.string.deleted_text_template, product.price.toPriceString()))
                }
            } else {
                tvPrice.text = product.price.toPriceString()
                tvPriceDeleted.isVisible = false
            }
            tvDiscountPercent.run {
                isVisible = product.discountRate > 0
                text = context.getString(R.string.product_card_discount_template, product.discountRate)
            }
            tvDesription.run {
                isVisible = product.description.isNullOrBlank().not()
                text = product.description
            }
            layoutBrand.run {
                root.isVisible = product.brand?.let {
                    ivLogo.load(it.logo, R.drawable.ic_logo, resizeAsItem = true)
                    true
                } ?: false
            }
            content.isVisible = true
        }
    }

    private fun populateProductColorItems(items: List<ProductColorItem>) {
        val selectedColorName = items.find { it.selected }?.productColor?.color?.name ?: ""
        binding.tvColors.setHtmlText(getString(R.string.product_card_color_placeholder, selectedColorName))
        productColorsAdapter.submitList(items)
    }

    private fun populateProductSizes(sizes: List<ProductSize>) {
        val productIsAvailable = sizes.any { it.isAvailable }
        binding.btnAddToCart.run {
            isEnabled = productIsAvailable
            setText(if (productIsAvailable) R.string.product_card_add_to_cart else R.string.product_card_not_available)
        }
        productSizesAdapter.submitList(sizes) {
            binding.groupSizes.isVisible = sizes.isNotEmpty()
        }
    }

    @Parcelize
    data class Args(
        val productId: String
    ) : Parcelable

    companion object {

        fun create(productId: String) = ProductCardFragment().withArgs(Args(productId))
    }
}
