package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.os.Parcelable
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentProductCardBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.HorizontalMarginItemDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.view.ToolbarView
import ru.babaetskv.passionwoman.app.utils.*
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem
import kotlin.math.abs

class ProductCardFragment : BaseFragment<ProductCardViewModel, ProductCardViewModel.Router, ProductCardFragment.Args>() {
    private val binding: FragmentProductCardBinding by viewBinding()
    private val productPhotosAdapter: ProductPhotosAdapter by lazy {
        ProductPhotosAdapter()
    }
    private val productSizesAdapter: ProductSizesAdapter by lazy {
        ProductSizesAdapter(viewModel::onSizeItemPressed)
    }
    private val colorsAdapter: ColorsAdapter by lazy {
        ColorsAdapter(viewModel::onColorItemPressed)
    }

    override val layoutRes: Int = R.layout.fragment_product_card
    override val viewModel: ProductCardViewModel by viewModel<ProductCardViewModelImpl> {
        parametersOf(args)
    }
    override val screenName: String = ScreenKeys.PRODUCT_CARD

    override fun initViews() {
        super.initViews()
        binding.run {
            toolbar.run {
                setStartActions(
                    ToolbarView.Action(
                        iconRes = R.drawable.ic_back,
                        onClick = viewModel::onBackPressed
                    )
                )
                this@ProductCardFragment.setEndActions()
            }
            vpPhotos.run {
                adapter = productPhotosAdapter
                offscreenPageLimit = 1
                val nextItemVisiblePx = dimen(R.dimen.margin_default)
                val currentItemHorizontalMarginPx = dimen(R.dimen.margin_large)
                val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
                val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
                    page.translationX = -pageTranslationX * position
                    page.scaleY = 1 - (0.25f * abs(position))
                    // page.alpha = 0.25f + (1 - abs(position))
                }
                setPageTransformer(pageTransformer)
                addItemDecoration(HorizontalMarginItemDecoration(context, R.dimen.margin_large))
            }
            rvColors.run {
                adapter = colorsAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
            }
            rvSizes.run {
                adapter = productSizesAdapter
                itemAnimator = null
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
        viewModel.colorsLiveData.observe(viewLifecycleOwner, ::populateColorItems)
        viewModel.productPhotosLiveData.observe(viewLifecycleOwner, ::populateProductPhotos)
        viewModel.productSizesLiveData.observe(viewLifecycleOwner, ::populateProductSizeItems)
        viewModel.isFavoriteLiveData.observe(viewLifecycleOwner, ::populateFavorite)
    }

    private fun populateFavorite(isFavorite: Boolean) {
        setEndActions(isFavorite)
    }

    private fun setEndActions(isFavorite: Boolean = false) {
        binding.toolbar.setEndActions(
            ToolbarView.Action(
                iconRes = R.drawable.ic_share,
                onClick = viewModel::onSharePressed
            ),
            ToolbarView.Action(
                iconRes = if (isFavorite) R.drawable.ic_like_checked else R.drawable.ic_like_unchecked,
                tintRes = R.color.favorite,
                onClick = viewModel::onFavoritePressed
            )
        )
    }

    private fun populateProductPhotos(photos: List<ProductImageItem>) {
        productPhotosAdapter.submitList(photos)
    }

    private fun populateProduct(product: Product) {
        binding.run {
            tvProductName.text = product.name
            ratingBar.rating = product.rating
            if (product.discountRate > 0) {
                tvPrice.text = product.priceWithDiscount.toFormattedString()
                tvPriceDeleted.run {
                    isVisible = true
                    setHtmlText(getString(R.string.deleted_text_template, product.price.toFormattedString()))
                }
            } else {
                tvPrice.text = product.price.toFormattedString()
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

    private fun populateColorItems(items: List<SelectableItem<Color>>) {
        val selectedColorName = items.find { it.isSelected }?.value?.uiName ?: ""
        binding.tvColors.setHtmlText(getString(R.string.product_card_color_placeholder, selectedColorName))
        colorsAdapter.submitList(items)
    }

    private fun populateProductSizeItems(items: List<SelectableItem<ProductSize>>) {
        val productIsAvailable = items.any { it.value.isAvailable }
        binding.btnAddToCart.run {
            isEnabled = productIsAvailable
            setText(if (productIsAvailable) R.string.product_card_add_to_cart else R.string.product_card_not_available)
        }
        productSizesAdapter.submitList(items) {
            binding.groupSizes.isVisible = items.isNotEmpty()
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
