package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductSizeBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

class ProductSizesAdapter(
    private val onItemClick: (ProductSizeItem) -> Unit
): BaseAdapter<ProductSizeItem>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductSizeItem> =
        LayoutInflater.from(parent.context)
            .let { ViewItemProductSizeBinding.inflate(it, parent, false) }
            .let { ViewHolder(it) }

    inner class ViewHolder(
        private val binding: ViewItemProductSizeBinding
    ) : BaseViewHolder<ProductSizeItem>(binding.root) {

        override fun bind(item: ProductSizeItem) {
            binding.run {
                root.setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
                tvSize.run {
                    text = item.size.value
                    isEnabled = item.size.isAvailable
                    isSelected = item.isSelected
                }
            }
        }
    }
}
