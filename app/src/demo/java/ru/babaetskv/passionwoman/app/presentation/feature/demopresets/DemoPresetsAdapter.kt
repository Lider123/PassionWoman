package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRadioButton
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemMultiPresetBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemSinglePresetBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback

class DemoPresetsAdapter(
    private val onPresetChanged: (DemoPreset) -> Unit
) : BaseAdapter<DemoPreset>(EqualDiffUtilCallback()) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DemoPreset.SingleDemoPreset -> SINGLE_SELECTION_VIEW_TYPE
        is DemoPreset.MultiDemoPreset -> MULTI_SELECTION_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DemoPreset> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SINGLE_SELECTION_VIEW_TYPE -> SingleSelectionViewHolder(inflater.inflate(R.layout.view_item_single_preset, parent, false))
            MULTI_SELECTION_VIEW_TYPE -> MultiSelectionViewHolder(inflater.inflate(R.layout.view_item_multi_preset, parent, false))
            else -> throw IllegalStateException("Unknown view type")
        }
    }

    inner class SingleSelectionViewHolder(v: View) : BaseViewHolder<DemoPreset>(v) {
        private val binding = ViewItemSinglePresetBinding.bind(v)

        override fun bind(item: DemoPreset) {
            item as DemoPreset.SingleDemoPreset
            binding.switchPreset.run {
                setText(item.titleRes)
                isChecked = item.value as Boolean
                setOnCheckedChangeListener { _, isChecked ->
                    val newItem = item.copy(
                        value = isChecked
                    )
                    onPresetChanged.invoke(newItem)
                }
            }
        }
    }

    inner class MultiSelectionViewHolder(v: View) : BaseViewHolder<DemoPreset>(v) {
        private val binding = ViewItemMultiPresetBinding.bind(v)

        override fun bind(item: DemoPreset) {
            item as DemoPreset.MultiDemoPreset
            binding.run {
                tvTitle.setText(item.titleRes)
                item.availableValuesWithTitles.forEach { availableValue ->
                    val radioButton = AppCompatRadioButton(context).apply {
                        id = View.generateViewId()
                        setText(availableValue.first)
                        isChecked = availableValue.second == item.value
                        setOnClickListener {
                            val newItem = item.copy(
                                value = availableValue.second
                            )
                            onPresetChanged.invoke(newItem)
                        }
                    }
                    radioGroup.addView(radioButton)
                }
            }
        }
    }

    companion object {
        private const val SINGLE_SELECTION_VIEW_TYPE = 0
        private const val MULTI_SELECTION_VIEW_TYPE = 1
    }
}
