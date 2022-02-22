package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemBrandBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.inflateLayout
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Brand

class BrandsAdapter(
    private val onItemClick: (Brand) -> Unit
) : BaseAdapter<Brand>(BrandsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Brand> =
        ViewHolder(parent.inflateLayout(R.layout.view_item_brand))

    inner class ViewHolder(v: View) : BaseViewHolder<Brand>(v) {
        private val binding = ViewItemBrandBinding.bind(v)

        override fun bind(item: Brand) {
            binding.run {
                root.setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
                ivLogo.load(item.logo, R.drawable.photo_placeholder, resizeAsItem = true)
            }
        }
    }

    private class BrandsDiffUtilCallback : DiffUtil.ItemCallback<Brand>() {

        override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean =
            oldItem.id == newItem.id
    }
}
