package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemCategoryBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.domain.model.Category

class CategoriesAdapter(
    private val onItemClick: (Category) -> Unit
) : BaseAdapter<Category>(CategoryDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Category> =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_category, parent, false)
            .let {
                ViewHolder(it)
            }
    
    inner class ViewHolder(v: View) : BaseViewHolder<Category>(v) {
        private val binding = ViewItemCategoryBinding.bind(v)

        override fun bind(item: Category) {
            binding.run {
                root.setOnClickListener {
                    onItemClick.invoke(item)
                }
                tvName.text = item.name
                ivImage.load(item.image, R.drawable.logo,
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
