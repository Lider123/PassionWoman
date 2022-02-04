package ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.databinding.ViewItemFilterColorBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.view.ColorView
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class FilterColorsAdapter(
    private val onItemClick: (SelectableItem<Color>) -> Unit
) : BaseAdapter<SelectableItem<Color>>(FilterColorDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SelectableItem<Color>> =
        ViewItemFilterColorBinding.inflate(LayoutInflater.from(parent.context), parent, false).let {
            ViewHolder(it)
        }

    inner class ViewHolder(
        private val binding: ViewItemFilterColorBinding
    ) : BaseViewHolder<SelectableItem<Color>>(binding.root) {
        private var item: SelectableItem<Color>? = null

        init {
            binding.root.setOnSingleClickListener {
                it as ColorView
                item?.copy(
                    isSelected = !it.borderVisible
                )?.let(onItemClick)
            }
        }

        override fun bind(item: SelectableItem<Color>) {
            this.item = item
            binding.root.run {
                val color = item.value
                if (color.isMulticolor) {
                    setMulticolor()
                } else setColor(color.hex.toColorInt())
                borderVisible = item.isSelected
            }
        }
    }

    class FilterColorDiffUtilCallback : DiffUtil.ItemCallback<SelectableItem<Color>>() {

        override fun areItemsTheSame(
            oldItem: SelectableItem<Color>,
            newItem: SelectableItem<Color>
        ): Boolean = oldItem.value.code == newItem.value.code

        override fun areContentsTheSame(
            oldItem: SelectableItem<Color>,
            newItem: SelectableItem<Color>
        ): Boolean = oldItem == newItem
    }
}
