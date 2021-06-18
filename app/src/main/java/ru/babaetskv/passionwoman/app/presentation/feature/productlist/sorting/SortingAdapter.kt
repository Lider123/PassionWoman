package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemSortingBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider

class SortingAdapter(
    private val stringProvider: StringProvider,
    private val onItemClick: (SortingItem) -> Unit
) : BaseAdapter<SortingItem>(SortingItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SortingItem> =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_sorting, parent, false)
            .let {
                ViewHolder(it)
            }

    private inner class ViewHolder(v: View) : BaseViewHolder<SortingItem>(v) {
        private val binding = ViewItemSortingBinding.bind(v)

        override fun bind(item: SortingItem) {
            binding.radioSorting.run {
                text = item.sorting.getUiName(stringProvider)
                isChecked = item.selected
                setOnClickListener {
                    onItemClick.invoke(item)
                }
            }
        }

        override fun bind(item: SortingItem, payload: Any) {
            if (payload == PAYLOAD_SELECTED) setSelected(item.selected)
        }

        private fun setSelected(selected: Boolean) {
            binding.radioSorting.isChecked = selected
        }
    }

    private class SortingItemDiffUtilCallback : DiffUtil.ItemCallback<SortingItem>() {

        override fun areContentsTheSame(oldItem: SortingItem, newItem: SortingItem): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: SortingItem, newItem: SortingItem): Boolean =
            oldItem.sorting == newItem.sorting

        override fun getChangePayload(oldItem: SortingItem, newItem: SortingItem): Any? {
            if (oldItem.selected != newItem.selected) return PAYLOAD_SELECTED

            return null
        }
    }

    companion object {
        private const val PAYLOAD_SELECTED = 0
    }
}
