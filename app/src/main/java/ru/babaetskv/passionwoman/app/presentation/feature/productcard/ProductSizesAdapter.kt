package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductSizeBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.ProductSize

class ProductSizesAdapter(
    private val onItemClick: (ProductSize) -> Unit
): BaseAdapter<ProductSize>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductSize> =
        LayoutInflater.from(parent.context)
            .let { ViewItemProductSizeBinding.inflate(it, parent, false) }
            .let { ViewHolder(it) }

    inner class ViewHolder(
        private val binding: ViewItemProductSizeBinding
    ) : BaseViewHolder<ProductSize>(binding.root) {

        override fun bind(item: ProductSize) {
            binding.run {
                root.setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
                btnSize.run {
                    text = item.value
                    isEnabled = item.isAvailable
                    // TODO: change stroke color when item is selected
                }
            }
        }
    }
}
