package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemColorBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.inflateLayout
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class ColorsAdapter(
    private val onItemClick: (item: SelectableItem<Color>) -> Unit
) : BaseAdapter<SelectableItem<Color>>(ColorItemDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SelectableItem<Color>> =
        ViewHolder(parent.inflateLayout(R.layout.view_item_color))

    inner class ViewHolder(v: View) : BaseViewHolder<SelectableItem<Color>>(v) {
        private val binding = ViewItemColorBinding.bind(v)

        override fun bind(item: SelectableItem<Color>) {
            binding.colorView.run {
                val color = item.value
                if (color.isMulticolor) {
                    setMulticolor()
                } else setColor(color.hex.toColorInt())
                borderVisible = item.isSelected
                setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
            }
        }

        override fun bind(item: SelectableItem<Color>, payload: Any) {
            if (payload == PAYLOAD_SELECTED) setSelected(item.isSelected)
        }

        private fun setSelected(selected: Boolean) {
            binding.colorView.borderVisible = selected
        }
    }

    private class ColorItemDiffUtilCallback :
        DiffUtil.ItemCallback<SelectableItem<Color>>() {

        override fun areContentsTheSame(
            oldItem: SelectableItem<Color>,
            newItem: SelectableItem<Color>
        ): Boolean = oldItem == newItem

        override fun areItemsTheSame(
            oldItem: SelectableItem<Color>,
            newItem: SelectableItem<Color>
        ): Boolean = oldItem.value == newItem.value

        override fun getChangePayload(
            oldItem: SelectableItem<Color>,
            newItem: SelectableItem<Color>
        ): Any? {
            if (oldItem.isSelected != newItem.isSelected) return PAYLOAD_SELECTED

            return null
        }
    }

    companion object {
        private const val PAYLOAD_SELECTED = 0
    }
}
