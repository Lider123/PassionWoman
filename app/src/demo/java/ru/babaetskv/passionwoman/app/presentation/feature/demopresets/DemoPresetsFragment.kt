package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentDemoPresetsBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.view.ToolbarView

class DemoPresetsFragment : BaseFragment<DemoPresetsViewModel, DemoPresetsFragment.Args>() {
    private val binding: FragmentDemoPresetsBinding by viewBinding()
    private val adapter: ListDelegationAdapter<List<DemoPreset>> by lazy {
        ListDelegationAdapter(
            singleDemoPresetDelegate(viewModel::onPresetChanged),
            multiDemoPresetDelegate(viewModel::onPresetChanged)
        )
    }

    override val layoutRes: Int = R.layout.fragment_demo_presets
    override val viewModel: DemoPresetsViewModel by viewModel<DemoPresetsViewModelImpl> {
        parametersOf(args)
    }
    override val screenName: String = ScreenKeys.DEMO_PRESETS

    override fun initViews() {
        super.initViews()
        binding.run {
            if (!args.onStart) {
                toolbar.setStartActions(
                    ToolbarView.Action(
                        iconRes = R.drawable.ic_back,
                        onClick = viewModel::onBackPressed
                    )
                )
            }
            rvPresets.run {
                adapter = this@DemoPresetsFragment.adapter
                addItemDecoration(EmptyDividerDecoration(context, R.dimen.margin_default))
            }
            btnApplyPresets.setOnClickListener {
                viewModel.onApplyPressed()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.presetsLiveData.observe(this, ::populatePresets)
    }

    private fun populatePresets(presets: List<DemoPreset>) {
        adapter.run {
            items = presets
            notifyDataSetChanged()
        }
    }

    @Parcelize
    data class Args(
        val onStart: Boolean
    ) : Parcelable

    companion object {

        fun newInstance(onStart: Boolean) =
            DemoPresetsFragment()
                .withArgs(Args(onStart))
    }
}