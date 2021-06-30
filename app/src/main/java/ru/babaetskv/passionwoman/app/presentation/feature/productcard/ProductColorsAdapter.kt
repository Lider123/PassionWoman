package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductColorBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

class ProductColorsAdapter(
    private val onItemClick: (item: ProductColorItem) -> Unit
) : BaseAdapter<ProductColorItem>(ProductColorItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductColorItem> =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_product_color, parent, false)
            .let {
                ViewHolder(it)
            }

    inner class ViewHolder(v: View) : BaseViewHolder<ProductColorItem>(v) {
        private val binding = ViewItemProductColorBinding.bind(v)

        override fun bind(item: ProductColorItem) {
            binding.colorView.run {
                setColor(item.productColor.color.hex.toColorInt())
                borderVisible = item.selected
                setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
            }
        }

        override fun bind(item: ProductColorItem, payload: Any) {
            if (payload == PAYLOAD_SELECTED) setSelected(item.selected)
        }

        private fun setSelected(selected: Boolean) {
            binding.colorView.borderVisible = selected
        }
    }

    private class ProductColorItemDiffUtilCallback : DiffUtil.ItemCallback<ProductColorItem>() {

        override fun areContentsTheSame(oldItem: ProductColorItem, newItem: ProductColorItem): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: ProductColorItem, newItem: ProductColorItem): Boolean =
            oldItem.productColor == newItem.productColor

        override fun getChangePayload(oldItem: ProductColorItem, newItem: ProductColorItem): Any? {
            if (oldItem.selected != newItem.selected) return PAYLOAD_SELECTED

            return null
        }
    }

    companion object {
        private const val PAYLOAD_SELECTED = 0
    }
}
