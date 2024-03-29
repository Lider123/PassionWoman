package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentCatalogBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.presentation.view.ToolbarView
import ru.babaetskv.passionwoman.app.utils.integer
import ru.babaetskv.passionwoman.domain.model.Category

class CatalogFragment :
    BaseFragment<CatalogViewModel, FragmentComponent.NoArgs>() {
    private val binding: FragmentCatalogBinding by viewBinding()
    private val categoriesAdapter: CategoriesAdapter by lazy {
        CategoriesAdapter(viewModel::onCategoryPressed)
    }

    override val viewModel: CatalogViewModel by viewModel<CatalogViewModelImpl>()
    override val layoutRes: Int = R.layout.fragment_catalog
    override val applyBottomInset: Boolean = false
    override val screenName: String = ScreenKeys.CATEGORIES

    override fun initViews() {
        super.initViews()
        binding.run {
            toolbar.setEndActions(
                ToolbarView.Action(
                    iconRes = R.drawable.ic_search,
                    onClick = viewModel::onSearchPressed
                )
            )
            rvCategories.run {
                layoutManager = GridLayoutManager(
                    requireContext(),
                    integer(R.integer.categories_list_span_count),
                    integer(R.integer.categories_list_orientation),
                    false
                )
                adapter = categoriesAdapter
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.categoriesLiveData.observe(viewLifecycleOwner, ::populateCategories)
    }

    private fun populateCategories(categories: List<Category>) {
        categoriesAdapter.submitList(categories) {
            binding.rvCategories.isVisible = categories.isNotEmpty()
        }
    }

    companion object {

        fun create() = CatalogFragment()
    }
}
