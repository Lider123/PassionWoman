package ru.babaetskv.passionwoman.app.presentation.feature.profile

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemProfileMenuBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.ProfileMenuItem
import ru.babaetskv.passionwoman.app.utils.inflateLayout
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

class ProfileMenuItemAdapter(
    private val onMenuItemClick: (item: ProfileMenuItem) -> Unit
) : BaseAdapter<ProfileMenuItem>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ProfileMenuItem> =
        ViewHolder(parent.inflateLayout(R.layout.view_item_profile_menu))

    inner class ViewHolder(v: View) : BaseViewHolder<ProfileMenuItem>(v) {
        private val binding = ViewItemProfileMenuBinding.bind(v)

        override fun bind(item: ProfileMenuItem) {
            binding.run {
                cardProfileMenuItem.setOnSingleClickListener {
                    onMenuItemClick.invoke(item)
                }
                tvMenuItem.run {
                    setCompoundDrawablesWithIntrinsicBounds(
                        item.getIcon(context),
                        null,
                        ContextCompat.getDrawable(context, R.drawable.ic_forward),
                        null
                    )
                    text = item.getTitle(context)
                }
            }
        }
    }
}
