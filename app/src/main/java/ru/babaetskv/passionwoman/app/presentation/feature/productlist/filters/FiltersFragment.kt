package ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentFiltersBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseBottomSheetDialogFragment
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.filters.Filter

// TODO: fix filters application for tablets
class FiltersFragment :
    BaseBottomSheetDialogFragment<FiltersViewModel, FiltersViewModel.Router, FiltersFragment.Args>() {
    private val binding: FragmentFiltersBinding by viewBinding()
    private val adapter: FiltersAdapter by lazy {
        FiltersAdapter(viewModel::onFilterChanged)
    }

    override val layoutRes: Int = R.layout.fragment_filters
    override val screenName: String = ScreenKeys.FILTERS
    override val viewModel: FiltersViewModel by viewModel<FiltersViewModelImpl> {
        parametersOf(args)
    }

    override fun initViews() {
        super.initViews()
        binding.run {
            rvFilters.run {
                rvFilters.run {
                    adapter = this@FiltersFragment.adapter
                    addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
                }
                btnApplyFilters.setOnSingleClickListener {
                    viewModel.onApplyFiltersPressed()
                }
                btnClearFilters.setOnSingleClickListener {
                    viewModel.onClearFiltersPressed()
                }
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.filtersLiveData.observe(viewLifecycleOwner, ::populateFilters)
        viewModel.productsCountLiveData.observe(viewLifecycleOwner, ::populateProductsCount)
    }

    private fun populateFilters(filters: List<Filter>) {
        adapter.submitList(filters.sortedByDescending(Filter::priority))
    }

    private fun populateProductsCount(count: Int) {
        binding.btnApplyFilters.run {
            text = resources.getQuantityString(R.plurals.filters_apply_placeholder, count, count)
            isEnabled = count > 0
        }
    }

    @Parcelize
    data class Args(
        val categoryId: Int?,
        val filters: List<Filter>,
        val initialProductsCount: Int
    ) : Parcelable

    companion object {

        fun create(
            categoryId: Int?,
            filters: List<Filter>,
            initialProductsCount: Int
        ) =
            FiltersFragment().withArgs(Args(
                categoryId = categoryId,
                filters = filters,
                initialProductsCount = initialProductsCount
            ))
    }
}
