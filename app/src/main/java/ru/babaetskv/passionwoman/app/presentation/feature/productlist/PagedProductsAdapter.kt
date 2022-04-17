package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductBinding
import ru.babaetskv.passionwoman.app.presentation.base.BasePagingAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.*
import ru.babaetskv.passionwoman.domain.model.Product

class PagedProductsAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onBuyClick: (Product) -> Unit
) : BasePagingAdapter<Product>(ProductDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Product> =
        ViewHolder(parent.viewBinding(ViewItemProductBinding::inflate), onItemClick, onBuyClick)

    class ViewHolder(
        private val binding: ViewItemProductBinding,
        onItemClick: (Product) -> Unit,
        onBuyClick: (Product) -> Unit
    ) : BaseViewHolder<Product>(binding.root) {
        private var item: Product? = null

        init {
            binding.run {
                cardPreview.setOnSingleClickListener {
                    item?.let(onItemClick)
                }
                btnBuy.setOnSingleClickListener {
                    item?.let(onBuyClick)
                }
            }
        }

        override fun bind(item: Product) {
            this.item = item
            binding.run {
                root.disabled = !item.isAvailable
                if (item.discountRate > 0) {
                    tvPrice.text = item.priceWithDiscount.toFormattedString()
                    tvPriceDeleted.run {
                        isVisible = true
                        setHtmlText(context.getString(R.string.deleted_text_template, item.price.toFormattedString()))
                    }
                } else {
                    tvPrice.text = item.price.toFormattedString()
                    tvPriceDeleted.isVisible = false
                }
                ratingBar.rating = item.rating
                tvName.text = item.name
                ivPreview.load(item.preview, R.drawable.photo_placeholder,
                    resizeAsItem = true
                )
                btnBuy.run {
                    isEnabled = item.isAvailable
                    setText(if (item.isAvailable) R.string.item_product_button_buy else R.string.product_card_not_available)
                }
                tvDiscountPercent.run {
                    isVisible = item.discountRate > 0
                    text = context.getString(R.string.product_card_discount_template, item.discountRate)
                }
            }
        }
    }

    private class ProductDiffUtilCallback : DiffUtil.ItemCallback<Product>() {

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id
    }
}