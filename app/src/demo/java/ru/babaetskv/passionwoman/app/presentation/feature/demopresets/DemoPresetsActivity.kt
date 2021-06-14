package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.content.Intent
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ActivityDemoPresetsBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.MainActivity
import ru.babaetskv.passionwoman.app.presentation.base.BaseActivity

class DemoPresetsActivity : BaseActivity<DemoPresetsViewModel>() {
    private val binding: ActivityDemoPresetsBinding by viewBinding()
    private val adapter: DemoPresetsAdapter by lazy {
        DemoPresetsAdapter(viewModel::onPresetChanged)
    }

    override val contentViewRes: Int = R.layout.activity_demo_presets
    override val viewModel: DemoPresetsViewModel by viewModel()

    override fun initView() {
        super.initView()
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
        lifecycleScope.launchWhenResumed {
            viewModel.eventBus.collect(::handleEvent)
        }
    }

    private fun handleEvent(event: DemoPresetsViewModel.Event) {
        when (event) {
            DemoPresetsViewModel.Event.LaunchMainFlow -> {
                startActivity(Intent(this@DemoPresetsActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun populatePresets(presets: List<DemoPreset>) {
        adapter.submitList(presets)
    }
}