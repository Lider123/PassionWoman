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
import timber.log.Timber

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
            binding.run {
                imgPreview.load(item.preview, R.drawable.photo_placeholder,
                    resizeAsItem = true
                )
                tvTitle.text = item.name
                if (item.price != item.priceWithDiscount) {
                    tvPrice.text = item.priceWithDiscount.toFormattedString()
                    tvPriceDeleted.run {
                        isVisible = true
                        setHtmlText(context.getString(R.string.deleted_text_template, item.price.toFormattedString()))
                    }
                } else {
                    tvPrice.text = item.price.toFormattedString()
                    tvPriceDeleted.isVisible = false
                }
                viewColor.run {
                    val color = item.selectedColor
                    if (color.isMulticolor) {
                        setMulticolor()
                    } else setColor(color.hex.toColorInt())
                    borderVisible = true
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
                    setItemCount(item.count)
                }
            }
        }

        override fun bind(item: CartItem, payload: Any) {
            if (payload == PAYLOAD_COUNT) setItemCount(item.count)
        }

        private fun setItemCount(count: Int) {
            binding.layoutCounter.tvCounter.text = count.toString()
        }
    }

    class CartItemDiffUtilCallback : DiffUtil.ItemCallback<CartItem>() {
        // TODO: after data updates. Counter stays the same

        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            (oldItem.productId == newItem.productId
                    && oldItem.selectedColor.code == newItem.selectedColor.code
                    && oldItem.selectedSize.value == newItem.selectedSize.value
                    && oldItem.count == newItem.count).also {
                        Timber.e("areItemsTheSame = $it") // TODO: remove
            }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            (oldItem == newItem).also {
                Timber.e("areContentsTheSame = $it") // TODO: remove
            }

        override fun getChangePayload(oldItem: CartItem, newItem: CartItem): Any? {
            Timber.e("getChangedPayload(${oldItem.count != newItem.count})") // TODO: remove
            if (oldItem.count != newItem.count) return PAYLOAD_COUNT

            return null
        }
    }

    companion object {
        private const val PAYLOAD_COUNT = 1
    }
}
