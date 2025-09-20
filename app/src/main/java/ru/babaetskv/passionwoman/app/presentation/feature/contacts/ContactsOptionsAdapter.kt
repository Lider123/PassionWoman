package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ru.babaetskv.passionwoman.app.databinding.ItemContactsOptionBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.viewBinding

class ContactsOptionsAdapter(
    private val onClick: (ContactsOption) -> Unit
) : BaseAdapter<ContactsOption>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ContactsOption> =
        ViewHolder(parent.viewBinding(ItemContactsOptionBinding::inflate))

    inner class ViewHolder(
        private val binding: ItemContactsOptionBinding
    ) : BaseViewHolder<ContactsOption>(binding.root) {

        override fun bind(item: ContactsOption) {
            binding.btnOption.run {
                text = context.getString(item.titleRes)
                icon = ContextCompat.getDrawable(context, item.iconRes)
                setOnSingleClickListener {
                    onClick.invoke(item)
                }
            }
        }
    }
}
