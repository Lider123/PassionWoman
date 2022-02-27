package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentCatalogBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.domain.model.Category

class CatalogFragment :
    BaseFragment<CatalogViewModel, CatalogViewModel.Router, FragmentComponent.NoArgs>() {
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
            toolbar.setOnEndClickListener {
                viewModel.onSearchPressed()
            }
            rvCategories.run {
                adapter = categoriesAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_default))
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.categoriesLiveData.observe(viewLifecycleOwner, ::populateCategories)
    }

    override fun handleRouterEvent(event: CatalogViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            is CatalogViewModel.Router.CategoryScreen -> {
                router.navigateTo(Screens.category(event.category))
            }
            is CatalogViewModel.Router.SearchScreen -> {
                router.navigateTo(Screens.search())
            }
        }
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
