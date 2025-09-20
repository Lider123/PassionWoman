package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.inflateLayout
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setHtmlText
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Product

class FavoritesAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onBuyClick: (Product) -> Unit,
    private val itemWidthRatio: Float = 1f
) : BaseAdapter<Product>(ProductDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Product> {
        val view = parent.inflateLayout(R.layout.view_item_product)
        if (itemWidthRatio < 0 && itemWidthRatio > 1) throw IllegalStateException("Item width ratio should be from 0 to 1")

        view.updateLayoutParams {
            width = if (itemWidthRatio != 1f) (itemWidthRatio * parent.measuredWidth).toInt() else width
        }
        return ViewHolder(view)
    }

    inner class ViewHolder(v: View) : BaseViewHolder<Product>(v) {
        private val binding = ViewItemProductBinding.bind(v)
        private var item: Product? = null

        init {
            binding.cardPreview.setOnSingleClickListener {
                item?.let(onItemClick)
            }
        }

        override fun bind(item: Product) {
            this.item = item
            binding.run {
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
                btnBuy.setOnSingleClickListener {
                    onBuyClick.invoke(item)
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
