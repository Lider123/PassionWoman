package ru.babaetskv.passionwoman.app.presentation.feature.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemProfileMenuBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

class ProfileMenuItemAdapter(
    private val onMenuItemClick: (item: ProfileMenuItem) -> Unit
) : BaseAdapter<ProfileMenuItem>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ProfileMenuItem> =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_profile_menu, parent, false)
            .let {
                ViewHolder(it)
            }

    inner class ViewHolder(v: View) : BaseViewHolder<ProfileMenuItem>(v) {
        private val binding = ViewItemProfileMenuBinding.bind(v)

        override fun bind(item: ProfileMenuItem) {
            binding.run {
                root.setOnSingleClickListener {
                    onMenuItemClick.invoke(item)
                }
                ivIcon.setImageResource(item.iconRes)
                tvTitle.setText(item.titleRes)
            }
        }
    }
}
