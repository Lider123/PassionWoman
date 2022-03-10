package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.content.Intent
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.ActivityDemoPresetsBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.MainActivity
import ru.babaetskv.passionwoman.app.presentation.base.BaseActivity

class DemoPresetsActivity : BaseActivity<DemoPresetsViewModel, DemoPresetsViewModelImpl.Router>() {
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
        val splashScreen = installSplashScreen()

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

    override fun handleRouterEvent(event: DemoPresetsViewModelImpl.Router) {
        super.handleRouterEvent(event)
        when (event) {
            DemoPresetsViewModelImpl.Router.MainFlow -> {
                startActivity(Intent(this@DemoPresetsActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun populatePresets(presets: List<DemoPreset>) {
        adapter.run {
            items = presets
            notifyDataSetChanged()
        }
    }
}