package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductSizeBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class ProductSizesAdapter(
    private val onItemClick: (SelectableItem<ProductSize>) -> Unit
): BaseAdapter<SelectableItem<ProductSize>>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SelectableItem<ProductSize>> =
        LayoutInflater.from(parent.context)
            .let { ViewItemProductSizeBinding.inflate(it, parent, false) }
            .let { ViewHolder(it) }

    inner class ViewHolder(
        private val binding: ViewItemProductSizeBinding
    ) : BaseViewHolder<SelectableItem<ProductSize>>(binding.root) {

        override fun bind(item: SelectableItem<ProductSize>) {
            binding.run {
                root.setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
                btnSize.run {
                    text = item.value.value
                    isEnabled = item.value.isAvailable
                    isChecked = item.isSelected
                }
            }
        }
    }
}
