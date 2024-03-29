package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentSortingBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class SortingFragment : BaseFragment<SortingViewModel, SortingFragment.Args>() {
    private val binding: FragmentSortingBinding by viewBinding()
    private val sortingAdapter: SortingAdapter by lazy {
        SortingAdapter(viewModel.stringProvider, viewModel::onSortingPressed)
    }

    override val layoutRes: Int = R.layout.fragment_sorting
    override val viewModel: SortingViewModel by viewModel<SortingViewModelImpl> {
        parametersOf(args)
    }
    override val screenName: String = ScreenKeys.SORTING

    override fun initViews() {
        super.initViews()
        binding.run {
            rvSortings.run {
                adapter = sortingAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
            }
            btnApplySorting.setOnSingleClickListener {
                viewModel.onApplySortingPressed()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.sortingsLiveData.observe(viewLifecycleOwner, ::populateSortings)
    }

    private fun populateSortings(sortings: List<SelectableItem<Sorting>>) {
        sortingAdapter.submitList(sortings)
    }

    @Parcelize
    data class Args(
        val sorting: Sorting
    ) : Parcelable

    companion object {

        fun create(sorting: Sorting) = SortingFragment().withArgs(Args(sorting))
    }
}
