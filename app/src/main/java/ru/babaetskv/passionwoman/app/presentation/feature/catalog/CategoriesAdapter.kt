package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemCategoryBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.inflateLayout
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Category

class CategoriesAdapter(
    private val onItemClick: (Category) -> Unit
) : BaseAdapter<Category>(CategoryDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Category> =
        ViewHolder(parent.inflateLayout(R.layout.view_item_category))
    
    inner class ViewHolder(v: View) : BaseViewHolder<Category>(v) {
        private val binding = ViewItemCategoryBinding.bind(v)

        override fun bind(item: Category) {
            binding.run {
                cardCategory.setOnSingleClickListener {
                    onItemClick.invoke(item)
                }
                tvName.text = item.name
                ivImage.load(item.image, R.drawable.photo_placeholder,
                    resizeAsItem = true
                )
            }
        }
    }

    private class CategoryDiffUtilCallback : DiffUtil.ItemCallback<Category>() {

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem.id == newItem.id
    }
}
