package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemStoryBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Story

class StoriesAdapter(
    private val onItemClick: (Story) -> Unit,
    private val itemWidthRatio: Float = 1f
) : BaseAdapter<Story>(StoriesDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Story> =
        ViewItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .let {
                if (itemWidthRatio < 0 && itemWidthRatio > 1) {
                    throw IllegalStateException("Item width ratio should be from 0 to 1")
                }

                it.root.updateLayoutParams {
                    width = if (itemWidthRatio != 1f) {
                        (itemWidthRatio * parent.measuredWidth).toInt()
                    } else width
                }
                ViewHolder(it)
            }

    inner class ViewHolder(
        private val binding: ViewItemStoryBinding
    ) : BaseViewHolder<Story>(binding.root) {
        private var item: Story? = null

        init {
            binding.root.setOnSingleClickListener {
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