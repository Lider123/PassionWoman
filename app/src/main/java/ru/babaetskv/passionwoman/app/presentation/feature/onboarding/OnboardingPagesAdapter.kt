package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.isVisible
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemOnboardingPageBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setInsetsListener
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

class OnboardingPagesAdapter(
    private val insets: WindowInsets?
) : BaseAdapter<OnboardingPage>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<OnboardingPage> =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_onboarding_page, parent, false)
            .let {
                ViewHolder(it)
            }

    inner class ViewHolder(v: View) : BaseViewHolder<OnboardingPage>(v) {
        private val binding = ViewItemOnboardingPageBinding.bind(v)

        init {
            binding.contentInsetsView.run {
                setInsetsListener()
                onApplyWindowInsets(insets)
            }
        }

        override fun bind(item: OnboardingPage) {
            binding.run {
                ivBanner.load(item.bannerRes)
                tvMessage.text = context.getString(item.messageRes)
                btnAction.run {
                    isVisible = item.actionRes != null
                    text = item.actionRes?.let { context.getString(it) }
                    setOnSingleClickListener {
                        item.actionCallback?.invoke()
                    }
                }
            }
        }
    }
}
