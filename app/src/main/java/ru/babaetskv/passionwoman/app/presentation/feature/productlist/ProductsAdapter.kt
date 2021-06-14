package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.toPriceString
import ru.babaetskv.passionwoman.domain.model.Product

class ProductsAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onBuyClick: (Product) -> Unit,
    private val itemWidthRatio: Float = 1f
) : BaseAdapter<Product>(ProductDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Product> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_product, parent, false)
        if (itemWidthRatio < 0 && itemWidthRatio > 1) throw IllegalStateException("Item width ratio should be from 0 to 1")

        view.updateLayoutParams {
            width = if (itemWidthRatio != 1f) (itemWidthRatio * parent.measuredWidth).toInt() else width
        }
        return ViewHolder(view)
    }

    inner class ViewHolder(v: View) : BaseViewHolder<Product>(v) {
        private val binding = ViewItemProductBinding.bind(v)

        override fun bind(item: Product) {
            binding.run {
                root.setOnClickListener {
                    onItemClick.invoke(item)
                }
                tvPrice.text = item.price.toPriceString()
                ratingBar.rating = item.rating
                tvName.text = item.name
                ivPreview.load(item.preview, R.drawable.photo_placeholder,
                    resizeAsItem = true
                )
                btnBuy.setOnClickListener {
                    onBuyClick.invoke(item)
                }
            }
        }
    }

    private class ProductDiffUtilCallback : DiffUtil.ItemCallback<Product>() {

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id
    }
}
