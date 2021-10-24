package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ru.babaetskv.passionwoman.app.databinding.ItemContactsOptionBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

class ContactsOptionsAdapter(
    private val onClick: (ContactsOption) -> Unit
) : BaseAdapter<ContactsOption>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ContactsOption> =
        LayoutInflater.from(parent.context).let {
            ItemContactsOptionBinding.inflate(it)
        }.let {
            ViewHolder(it)
        }

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
