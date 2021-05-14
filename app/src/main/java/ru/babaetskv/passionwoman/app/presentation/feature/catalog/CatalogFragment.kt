package ru.babaetskv.passionwoman.app.presentation.feature.catalog

import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentCatalogBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.domain.model.Category

class CatalogFragment : BaseFragment<CatalogViewModel, BaseFragment.NoArgs>() {
    private val binding: FragmentCatalogBinding by viewBinding()

    override val viewModel: CatalogViewModel by viewModel()
    override val layoutRes: Int = R.layout.fragment_catalog

    private val categoriesAdapter: CategoriesAdapter by lazy {
        CategoriesAdapter(viewModel::onCategoryPressed)
    }

    override fun initViews() {
        super.initViews()
        binding.rvCategories.run {
            adapter = categoriesAdapter
            layoutAnimation =
                LayoutAnimationController(AnimationUtils.loadAnimation(requireContext(), R.anim.item_fade_in))
            addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_default))
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.categoriesLiveData.observe(viewLifecycleOwner, ::populateCategories)
    }

    private fun populateCategories(categories: List<Category>) {
        categoriesAdapter.submitList(categories) {
            binding.run {
                toolbar.isVisible = categories.isNotEmpty()
                rvCategories.isVisible = categories.isNotEmpty()
            }
        }
    }

    companion object {

        fun create() = CatalogFragment()
    }
}
