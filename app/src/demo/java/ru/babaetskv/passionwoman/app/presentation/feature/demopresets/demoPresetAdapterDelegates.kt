package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.view.View
import androidx.appcompat.widget.AppCompatRadioButton
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemMultiPresetBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemSinglePresetBinding

fun singleDemoPresetDelegate(onPresetChanged: (DemoPreset.SingleDemoPreset) -> Unit) =
    adapterDelegateViewBinding<DemoPreset.SingleDemoPreset, DemoPreset, ViewItemSinglePresetBinding>(
        { layoutInflater, parent ->
            ViewItemSinglePresetBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        bind {
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

fun multiDemoPresetDelegate(onPresetChanged: (DemoPreset.MultiDemoPreset) -> Unit) =
    adapterDelegateViewBinding<DemoPreset.MultiDemoPreset, DemoPreset, ViewItemMultiPresetBinding>(
        { layoutInflater, parent ->
            ViewItemMultiPresetBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        bind {
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
