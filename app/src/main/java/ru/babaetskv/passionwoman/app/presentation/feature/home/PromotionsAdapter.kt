package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemPromotionBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.domain.model.Promotion

class PromotionsAdapter(
    private val onItemPressed: (Promotion) -> Unit
): BaseAdapter<Promotion>(PromotionDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Promotion> =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_promotion, parent, false)
            .let {
                ViewHolder(it)
            }

    inner class ViewHolder(v: View) : BaseViewHolder<Promotion>(v) {
        private val binding = ViewItemPromotionBinding.bind(v)

        override fun bind(item: Promotion) {
            binding.run {
                root.setOnClickListener {
                    onItemPressed.invoke(item)
                }
                ivBanner.load(item.banner, R.drawable.photo_placeholder, resizeAsItem = true)
            }
        }
    }

    class PromotionDiffUtilCallback : DiffUtil.ItemCallback<Promotion>() {

        override fun areItemsTheSame(oldItem: Promotion, newItem: Promotion): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Promotion, newItem: Promotion): Boolean =
            oldItem == newItem
    }
}