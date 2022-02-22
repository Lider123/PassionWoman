package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewInputRangeBinding
import ru.babaetskv.passionwoman.app.utils.toFloat
import ru.babaetskv.passionwoman.app.utils.viewBinding
import java.lang.IllegalArgumentException

class InputRangeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = viewBinding(ViewInputRangeBinding::inflate, true)
    private var onChangeListener: OnChangeListener? = null
    private var inputStringFormatter: (Float) -> String = { it.toString() }

    var labelFrom: String
        get() = binding.tvFrom.text.toString()
        set(value) {
            binding.tvFrom.run {
                isVisible = value.isNotBlank()
                text = value
            }
        }
    var labelTo: String
        get() = binding.tvTo.text.toString()
        set(value) {
            binding.tvTo.isVisible = value.isNotBlank()
            binding.tvTo.text = value
        }
    var fromPrefix: String
        get() = binding.tvFromPrefix.text.toString()
        set(value) {
            binding.tvFromPrefix.text = value
        }
    var toPrefix: String
        get() = binding.tvToPrefix.text.toString()
        set(value) {
            binding.tvToPrefix.text = value
        }
    var min: Float
        get() = binding.rangeSlider.valueFrom
        private set(value) {
            binding.rangeSlider.valueFrom = value
        }
    var max: Float
        get() = binding.rangeSlider.valueTo
        private set(value) {
            binding.rangeSlider.valueTo = value
        }
    var selectedMin: Float
        get() = binding.rangeSlider.values[0]
        private set(value) {
            binding.inputFrom.setText(inputStringFormatter.invoke(value))
            updateRangeSelectedMin(value)
        }
    var selectedMax: Float
        get() = binding.rangeSlider.values[1]
        private set(value) {
            binding.inputTo.setText(inputStringFormatter.invoke(value))
            updateRangeSelectedMax(value)
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.InputRangeView, 0, 0).apply {
            try {
                labelFrom = getString(R.styleable.InputRangeView_irv_labelFrom) ?: ""
                labelTo = getString(R.styleable.InputRangeView_irv_labelTo) ?: ""
                fromPrefix = getString(R.styleable.InputRangeView_irv_fromPrefix) ?: ""
                toPrefix = getString(R.styleable.InputRangeView_irv_toPrefix) ?: ""
            } finally {
                recycle()
            }
        }
        binding.rangeSlider.addOnChangeListener { slider, _, fromUser ->
            if (!fromUser) return@addOnChangeListener

            selectedMin = slider.values[0]
            selectedMax = slider.values[1]
            onChangeListener?.onRangeChanged(selectedMin, selectedMax)
        }
        binding.inputFrom.doAfterTextChanged {
            val newValue = it.toFloat()
            val newParams = createRangeParams().copy(
                selectedMin = newValue
            )
            if (newParams.areCorrect) updateRangeSelectedMin(newValue)
        }
        binding.inputFrom.setOnFocusChangeListener { view, hasFocus ->
            view as EditText
            val inputValue = view.text.toFloat()
            if (!hasFocus && inputValue != selectedMin) {
                view.setText(inputStringFormatter.invoke(selectedMin))
            }
        }
        binding.inputTo.doAfterTextChanged {
            val newValue = it.toFloat()
            val newParams = createRangeParams().copy(
                selectedMax = newValue
            )
            if (newParams.areCorrect) updateRangeSelectedMax(newValue)
        }
        binding.inputTo.setOnFocusChangeListener { view, hasFocus ->
            view as EditText
            val inputValue = view.text.toFloat()
            if (!hasFocus && inputValue != selectedMax) {
                view.setText(inputStringFormatter.invoke(selectedMax))
            }
        }
    }

    private fun updateRangeSelectedMin(value: Float) {
        val newSliderValues = listOf(value, binding.rangeSlider.values[1])
        binding.rangeSlider.values = newSliderValues
    }

    private fun updateRangeSelectedMax(value: Float) {
        val newSliderValues = listOf(binding.rangeSlider.values[0], value)
        binding.rangeSlider.values = newSliderValues
    }

    private fun createRangeParams(): RangeParams =
        RangeParams(
            min = min,
            max = max,
            selectedMin = binding.rangeSlider.values[0],
            selectedMax = binding.rangeSlider.values[1]
        )

    fun setInputStringFormatter(formatter: (Float) -> String) {
        inputStringFormatter = formatter
    }

    fun setRangeParams(params: RangeParams) {
        if (!params.areCorrect) throw IllegalArgumentException("Range params $params are incorrect")

        min = params.min
        max = params.max
        binding.rangeSlider.values = listOf(params.selectedMin, params.selectedMax)
        selectedMin = params.selectedMin
        selectedMax = params.selectedMax
    }

    fun setOnChangeListener(listener: OnChangeListener) {
        onChangeListener = listener
    }

    fun removeOnChangeListener() {
        onChangeListener = null
    }

    data class RangeParams(
        val min: Float,
        val max: Float,
        val selectedMin: Float,
        val selectedMax: Float
    ) {
        val areCorrect: Boolean
            get() = max >= min
                && selectedMax >= selectedMin
                && selectedMin >= min
                && selectedMin <= max
                && selectedMax >= min
                && selectedMax <= max
    }

    fun interface OnChangeListener {
        fun onRangeChanged(selectedMin: Float, selectedMax: Float)
    }
}
