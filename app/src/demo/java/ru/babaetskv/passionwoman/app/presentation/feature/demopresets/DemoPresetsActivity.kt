package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.content.Intent
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.ActivityDemoPresetsBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.MainActivity
import ru.babaetskv.passionwoman.app.presentation.base.BaseActivity
import ru.babaetskv.passionwoman.app.presentation.event.Event

class DemoPresetsActivity : BaseActivity<DemoPresetsViewModel>() {
    private val binding: ActivityDemoPresetsBinding by viewBinding()
    private val adapter: ListDelegationAdapter<List<DemoPreset>> by lazy {
        ListDelegationAdapter(
            singleDemoPresetDelegate(viewModel::onPresetChanged),
            multiDemoPresetDelegate(viewModel::onPresetChanged)
        )
    }

    override val contentViewRes: Int = R.layout.activity_demo_presets
    override val viewModel: DemoPresetsViewModel by viewModel<DemoPresetsViewModelImpl>()
    override val applyInsets: Boolean = true
    override val screenName: String = ScreenKeys.DEMO_PRESETS

    override fun onCreate(savedInstanceState: Bundle?) {
        installAppSplashScreen {
            viewModel.splashScreenVisible
        }
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
        super.initViews()
        binding.run {
            rvPresets.run {
                adapter = this@DemoPresetsActivity.adapter
                addItemDecoration(EmptyDividerDecoration(this@DemoPresetsActivity, R.dimen.margin_default))
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

    override fun onEvent(event: Event) {
        when (event) {
            DemoPresetsViewModel.StartMainFlowEvent -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else -> super.onEvent(event)
        }
    }

    private fun populatePresets(presets: List<DemoPreset>) {
        adapter.run {
            items = presets
            notifyDataSetChanged()
        }
    }
}