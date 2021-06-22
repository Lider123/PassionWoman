package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentProductCardBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.setHtmlText
import ru.babaetskv.passionwoman.app.utils.toPriceString
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Product

class ProductCardFragment : BaseFragment<ProductCardViewModel, ProductCardViewModel.Router, ProductCardFragment.Args>() {
    private val binding: FragmentProductCardBinding by viewBinding()
    private val productPhotosAdapter: ProductPhotosAdapter by lazy {
        ProductPhotosAdapter()
    }
    private val productColorsAdapter: ProductColorsAdapter by lazy {
        ProductColorsAdapter(viewModel::onColorItemPressed)
    }

    override val layoutRes: Int = R.layout.fragment_product_card
    override val viewModel: ProductCardViewModel by viewModel {
        parametersOf(args)
    }

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
            btnAddToCart.setOnClickListener {
                viewModel.onAddToCartPressed()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.productLiveData.observe(viewLifecycleOwner, ::populateProduct)
        viewModel.productColorsLiveData.observe(viewLifecycleOwner, ::populateProductColorItems)
        viewModel.productPhotosLiveData.observe(viewLifecycleOwner, ::populateProductPhotos)
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
                tvPrice.setHtmlText(getString(R.string.item_product_price_with_discount_template, product.priceWithDiscount.toPriceString(), product.price.toPriceString()))
            } else {
                tvPrice.text = product.price.toPriceString()
            }
            tvDiscountPercent.run {
                isVisible = product.discountRate > 0
                text = context.getString(R.string.product_card_discount_template, product.discountRate)
            }
        }
    }

    private fun populateProductColorItems(items: List<ProductColorItem>) {
        val selectedColorName = items.find { it.selected }?.productColor?.color?.name ?: ""
        binding.tvColors.setHtmlText(getString(R.string.product_card_color_placeholder, selectedColorName))
        productColorsAdapter.submitList(items)
    }

    @Parcelize
    data class Args(
        val product: Product
    ) : Parcelable

    companion object {

        fun create(product: Product) = ProductCardFragment().withArgs(Args(product))
    }
}
