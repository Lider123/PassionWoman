package ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters

import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import kotlinx.coroutines.flow.*
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemBooleanFilterBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemColorFilterBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemMultiFilterBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemRangeFilterBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.presentation.view.InputRangeView
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.viewBinding
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.Price
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.model.filters.FilterValue
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

class FiltersAdapter(
    private val onFilterChanged: (Filter) -> Unit
) : BaseAdapter<Filter>(EqualDiffUtilCallback()) {

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Filter> =
        when (Filter.Type.values()[viewType]) {
            Filter.Type.BOOLEAN -> {
                BooleanFilterViewHolder(parent.viewBinding(ViewItemBooleanFilterBinding::inflate))
            }
            Filter.Type.COLOR -> {
                ColorFilterViewHolder(parent.viewBinding(ViewItemColorFilterBinding::inflate))
            }
            Filter.Type.RANGE -> {
                RangeFilterViewHolder(parent.viewBinding(ViewItemRangeFilterBinding::inflate))
            }
            Filter.Type.MULTI -> {
                MultiFilterViewHolder(parent.viewBinding(ViewItemMultiFilterBinding::inflate))
            }
        }

    inner class BooleanFilterViewHolder(
        private val binding: ViewItemBooleanFilterBinding
    ) : BaseViewHolder<Filter>(binding.root) {
        private var item: Filter.Bool? = null

        init {
            binding.switchFilter.setOnSingleClickListener { v ->
                v as SwitchCompat
                item?.apply {
                    value = v.isChecked
                }?.let(onFilterChanged)
            }
        }

        override fun bind(item: Filter) {
            this.item = item as Filter.Bool
            binding.switchFilter.run {
                text = item.uiName
                isChecked = item.value
            }
        }
    }

    inner class ColorFilterViewHolder(
        private val binding: ViewItemColorFilterBinding
    ) : BaseViewHolder<Filter>(binding.root) {
        private var item: Filter.ColorMulti? = null
        private val adapter: FilterColorsAdapter by lazy {
            FilterColorsAdapter(::onColorClick)
        }

        init {
            binding.rvColors.run {
                adapter = this@ColorFilterViewHolder.adapter
                addItemDecoration(EmptyDividerDecoration(context, R.dimen.margin_extra_small))
            }
        }

        override fun bind(item: Filter) {
            this.item = item as Filter.ColorMulti
            binding.tvTitle.text = item.uiName
            adapter.submitList(item.values)
        }

        private fun onColorClick(item: SelectableItem<Color>) {
            this.item?.let { filter ->
                val newValues = filter.values.map {
                    if (it.value.id == item.value.id) item else it
                }
                Filter.ColorMulti(
                    code = filter.code,
                    uiName = filter.uiName,
                    priority = filter.priority,
                    values = newValues
                ).let(onFilterChanged)
            }
        }
    }

    inner class RangeFilterViewHolder(
        private val binding: ViewItemRangeFilterBinding
    ) : BaseViewHolder<Filter>(binding.root) {
        private var item: Filter.Range? = null

        init {
            binding.rangeView.run {
                setOnChangeListener { selectedMin, selectedMax ->
                    item?.let {
                        Filter.Range(
                            code = it.code,
                            uiName = it.uiName,
                            priority = it.priority,
                            min = it.min,
                            max = it.max,
                            selectedMin = Price(selectedMin),
                            selectedMax = Price(selectedMax)
                        )
                    }?.let(onFilterChanged)
                }
                setInputStringFormatter { Price(it).toFormattedString(withCurrency = false) }
            }
        }

        override fun bind(item: Filter) {
            this.item = item as Filter.Range
            binding.tvTitle.text = item.uiName
            binding.rangeView.run {
                setRangeParams(InputRangeView.RangeParams(
                    min = floor(item.min.toFloat()),
                    max = ceil(item.max.toFloat()),
                    selectedMin = item.selectedMin.toFloat(),
                    selectedMax = item.selectedMax.toFloat()
                ))
            }
        }
    }

    inner class MultiFilterViewHolder(
        private val binding: ViewItemMultiFilterBinding
    ) : BaseViewHolder<Filter>(binding.root) {
        private var item: Filter.Multi? = null
        private val adapter: FilterValuesAdapter by lazy {
            FilterValuesAdapter(::onFilterValueClick)
        }

        init {
            binding.rvValues.run {
                adapter = this@MultiFilterViewHolder.adapter
                addItemDecoration(EmptyDividerDecoration(context, R.dimen.margin_extra_small))
            }
        }

        override fun bind(item: Filter) {
            this.item = item as Filter.Multi
            binding.run {
                tvTitle.text = item.uiName
                adapter.submitList(item.values)
            }
        }

        private fun onFilterValueClick(item: SelectableItem<FilterValue>) {
            this.item?.let { filter ->
                val newValues = filter.values.map {
                    if (it.value.code == item.value.code) item else it
                }
                Filter.Multi(
                    code = filter.code,
                    uiName = filter.uiName,
                    priority = filter.priority,
                    values = newValues
                ).let(onFilterChanged)
            }
        }
    }
}
