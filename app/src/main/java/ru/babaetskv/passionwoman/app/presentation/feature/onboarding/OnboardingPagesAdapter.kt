package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemOnboardingPageBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback

class OnboardingPagesAdapter: BaseAdapter<OnboardingPage>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<OnboardingPage> =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_onboarding_page, parent, false)
            .let {
                ViewHolder(it)
            }

    class ViewHolder(v: View) : BaseViewHolder<OnboardingPage>(v) {
        private val binding = ViewItemOnboardingPageBinding.bind(v)

        override fun bind(item: OnboardingPage) {
            binding.run {
                val context = itemView.context
                ivBanner.setImageResource(item.bannerRes)
                tvMessage.text = context.getString(item.messageRes)
                btnAction.run {
                    isVisible = item.actionRes != null
                    text = item.actionRes?.let { context.getString(it) }
                    setOnClickListener {
                        item.actionCallback?.invoke()
                    }
                }
            }
        }
    }
}
