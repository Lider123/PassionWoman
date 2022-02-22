package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductColorBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.inflateLayout
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.ProductColor
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class ProductColorsAdapter(
    private val onItemClick: (item: SelectableItem<ProductColor>) -> Unit
) : BaseAdapter<SelectableItem<ProductColor>>(ProductColorItemDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SelectableItem<ProductColor>> =
        ViewHolder(parent.inflateLayout(R.layout.view_item_product_color))

    inner class ViewHolder(v: View) : BaseViewHolder<SelectableItem<ProductColor>>(v) {
        private val binding = ViewItemProductColorBinding.bind(v)

        override fun bind(item: SelectableItem<ProductColor>) {
            binding.colorView.run {
                val color = item.value.color
                if (color.isMulticolor) {
                    setMulticolor()
                } else setColor(color.hex.toColorInt())
                borderVisible = item.isSelected
                setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
            }
        }

        override fun bind(item: SelectableItem<ProductColor>, payload: Any) {
            if (payload == PAYLOAD_SELECTED) setSelected(item.isSelected)
        }

        private fun setSelected(selected: Boolean) {
            binding.colorView.borderVisible = selected
        }
    }

    private class ProductColorItemDiffUtilCallback :
        DiffUtil.ItemCallback<SelectableItem<ProductColor>>() {

        override fun areContentsTheSame(
            oldItem: SelectableItem<ProductColor>,
            newItem: SelectableItem<ProductColor>
        ): Boolean = oldItem == newItem

        override fun areItemsTheSame(
            oldItem: SelectableItem<ProductColor>,
            newItem: SelectableItem<ProductColor>
        ): Boolean = oldItem.value == newItem.value

        override fun getChangePayload(
            oldItem: SelectableItem<ProductColor>,
            newItem: SelectableItem<ProductColor>
        ): Any? {
            if (oldItem.isSelected != newItem.isSelected) return PAYLOAD_SELECTED

            return null
        }
    }

    companion object {
        private const val PAYLOAD_SELECTED = 0
    }
}
