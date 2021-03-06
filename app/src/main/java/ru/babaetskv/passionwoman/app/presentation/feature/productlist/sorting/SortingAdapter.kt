package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.databinding.ViewItemSortingBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.viewBinding
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class SortingAdapter(
    private val stringProvider: StringProvider,
    private val onItemClick: (SelectableItem<Sorting>) -> Unit
) : BaseAdapter<SelectableItem<Sorting>>(SortingItemDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SelectableItem<Sorting>> =
        ViewHolder(parent.viewBinding(ViewItemSortingBinding::inflate))

    private inner class ViewHolder(
        private val binding: ViewItemSortingBinding
    ) : BaseViewHolder<SelectableItem<Sorting>>(binding.root) {

        override fun bind(item: SelectableItem<Sorting>) {
            binding.radioSorting.run {
                text = item.value.getUiName(stringProvider)
                isChecked = item.isSelected
                setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
            }
        }

        override fun bind(item: SelectableItem<Sorting>, payload: Any) {
            if (payload == PAYLOAD_SELECTED) setSelected(item.isSelected)
        }

        private fun setSelected(selected: Boolean) {
            binding.radioSorting.isChecked = selected
        }
    }

    private class SortingItemDiffUtilCallback : DiffUtil.ItemCallback<SelectableItem<Sorting>>() {

        override fun areContentsTheSame(
            oldItem: SelectableItem<Sorting>,
            newItem: SelectableItem<Sorting>
        ): Boolean = oldItem == newItem

        override fun areItemsTheSame(
            oldItem: SelectableItem<Sorting>,
            newItem: SelectableItem<Sorting>
        ): Boolean = oldItem.value == newItem.value

        override fun getChangePayload(
            oldItem: SelectableItem<Sorting>,
            newItem: SelectableItem<Sorting>
        ): Any? {
            if (oldItem.isSelected != newItem.isSelected) return PAYLOAD_SELECTED

            return null
        }
    }

    companion object {
        private const val PAYLOAD_SELECTED = 0
    }
}
