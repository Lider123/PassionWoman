package ru.babaetskv.passionwoman.app.presentation.feature.cart

import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemCartItemBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setHtmlText
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.viewBinding
import ru.babaetskv.passionwoman.domain.model.CartItem

class CartItemsAdapter(
    private val onAddClick: (CartItem) -> Unit,
    private val onRemoveClick: (CartItem) -> Unit,
) : BaseAdapter<CartItem>(CartItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CartItem> =
        ViewHolder(parent.viewBinding(ViewItemCartItemBinding::inflate))

    inner class ViewHolder(
        private val binding: ViewItemCartItemBinding
    ) : BaseViewHolder<CartItem>(binding.root) {

        override fun bind(item: CartItem) {
            val product = item.product
            binding.run {
                imgPreview.load(product.preview, R.drawable.photo_placeholder,
                    resizeAsItem = true
                )
                tvTitle.text = product.name
                if (product.discountRate > 0) {
                    tvPrice.text = product.priceWithDiscount.toFormattedString()
                    tvPriceDeleted.run {
                        isVisible = true
                        setHtmlText(context.getString(R.string.deleted_text_template, product.price.toFormattedString()))
                    }
                } else {
                    tvPrice.text = product.price.toFormattedString()
                    tvPriceDeleted.isVisible = false
                }
                viewColor.run {
                    val color = item.selectedColor
                    if (color.isMulticolor) {
                        setMulticolor()
                    } else setColor(color.hex.toColorInt())
                    borderVisible = false
                }
                viewSize.btnSize.run {
                    text = item.selectedSize.value
                    isEnabled = true
                    isChecked = true
                }
                layoutCounter.run {
                    btnAdd.setOnSingleClickListener {
                        onAddClick.invoke(item)
                    }
                    btnRemove.setOnSingleClickListener {
                        onRemoveClick.invoke(item)
                    }
                    tvCounter.text = item.count.toString()
                }
            }
        }
    }

    class CartItemDiffUtilCallback : DiffUtil.ItemCallback<CartItem>() {

        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem.product.id == newItem.product.id

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem == newItem
    }
}
