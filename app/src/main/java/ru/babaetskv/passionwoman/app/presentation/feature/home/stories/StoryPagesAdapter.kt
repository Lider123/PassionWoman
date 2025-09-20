package ru.babaetskv.passionwoman.app.presentation.feature.home.stories

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.babaetskv.passionwoman.domain.model.Story

class StoryPagesAdapter(
    fragment: Fragment,
    private val onCloseClickListener: () -> Unit,
    private val onPrevStoryClickListener: () -> Unit,
    private val onNextStoryClickListener: () -> Unit
) : FragmentStateAdapter(fragment) {
    private val data = mutableListOf<Story>()

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment =
        StoryPageFragment.create(data[position]).apply {
            clickListener = object : StoryPageFragment.ClickListener {

                override fun onClosePressed() {
                    onCloseClickListener.invoke()
                }

                override fun onPrevStoryPressed() {
                    onPrevStoryClickListener.invoke()
                }

                override fun onNextStoryPressed() {
                    onNextStoryClickListener.invoke()
                }
            }
        }

    fun submitData(data: List<Story>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}
