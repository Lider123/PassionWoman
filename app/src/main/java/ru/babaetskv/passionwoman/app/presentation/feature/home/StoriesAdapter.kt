package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemStoryBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.viewBinding
import ru.babaetskv.passionwoman.domain.model.Story

class StoriesAdapter(
    private val onItemClick: (Story) -> Unit
) : BaseAdapter<Story>(StoriesDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Story> =
        ViewHolder(parent.viewBinding(ViewItemStoryBinding::inflate))

    inner class ViewHolder(
        private val binding: ViewItemStoryBinding
    ) : BaseViewHolder<Story>(binding.root) {
        private var item: Story? = null

        init {
            binding.cardStory.setOnSingleClickListener {
                item?.let(onItemClick)
            }
        }

        override fun bind(item: Story) {
            this.item = item
            binding.ivBanner.load(item.banner, R.drawable.photo_placeholder, resizeAsItem = true)
        }
    }

    private class StoriesDiffUtilCallback : DiffUtil.ItemCallback<Story>() {

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean =
            oldItem.id == newItem.id
    }
}