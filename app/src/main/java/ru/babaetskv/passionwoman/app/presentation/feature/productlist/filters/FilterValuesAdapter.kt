package ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.databinding.ViewItemFilterValueBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem
import ru.babaetskv.passionwoman.domain.model.filters.FilterValue

class FilterValuesAdapter(
    private val onItemClicked: (SelectableItem<FilterValue>) -> Unit
) : BaseAdapter<SelectableItem<FilterValue>>(FilterValueDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SelectableItem<FilterValue>> =
        ViewItemFilterValueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let {
            ViewHolder(it)
        }

    inner class ViewHolder(
        private val binding: ViewItemFilterValueBinding
    ) : BaseViewHolder<SelectableItem<FilterValue>>(binding.root) {
        private var item: SelectableItem<FilterValue>? = null

        init {
            itemView.setOnSingleClickListener { v ->
                v as AppCompatToggleButton
                item?.copy(
                    isSelected = v.isChecked
                )?.let(onItemClicked)
            }
        }

        override fun bind(item: SelectableItem<FilterValue>) {
            this.item = item
            binding.root.run {
                text = item.value.uiName
                isChecked = item.isSelected
            }
        }
    }

    class FilterValueDiffUtilCallback : DiffUtil.ItemCallback<SelectableItem<FilterValue>>() {

        override fun areItemsTheSame(
            oldItem: SelectableItem<FilterValue>,
            newItem: SelectableItem<FilterValue>
        ): Boolean = oldItem.value.code == newItem.value.code

        override fun areContentsTheSame(
            oldItem: SelectableItem<FilterValue>,
            newItem: SelectableItem<FilterValue>
        ): Boolean {
            // It is needed to return false to prevent an issue when the toggle button remains its
            // state after items update
            return false
        }
    }
}
