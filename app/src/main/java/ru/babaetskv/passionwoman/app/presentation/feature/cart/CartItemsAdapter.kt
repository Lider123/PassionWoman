package ru.babaetskv.passionwoman.app.presentation.feature.cart

import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemCartItemBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.DiffUtilAdapter
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setHtmlText
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.viewBinding
import ru.babaetskv.passionwoman.domain.model.CartItem

class CartItemsAdapter(
    private val onAddClick: (CartItem) -> Unit,
    private val onRemoveClick: (CartItem) -> Unit,
) : DiffUtilAdapter<CartItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CartItem> =
        ViewHolder(parent.viewBinding(ViewItemCartItemBinding::inflate))

    override fun createDiffUtilCallback(
        oldList: List<CartItem>,
        newList: List<CartItem>
    ): Callback<CartItem> = CartItemDiffUtilCallback(oldList, newList)

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

    class CartItemDiffUtilCallback(
        oldList: List<CartItem>,
        newList: List<CartItem>
    ) : DiffUtilAdapter.Callback<CartItem>(oldList, newList) {

        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem.productId == newItem.productId
                    && oldItem.selectedColor.code == newItem.selectedColor.code
                    && oldItem.selectedSize.value == newItem.selectedSize.value

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: CartItem, newItem: CartItem): Any? {
            if (oldItem.count != newItem.count) return PAYLOAD_COUNT

            return null
        }
    }

    companion object {
        private const val PAYLOAD_COUNT = 1
    }
}
