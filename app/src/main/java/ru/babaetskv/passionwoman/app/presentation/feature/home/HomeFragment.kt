package ru.babaetskv.passionwoman.app.presentation.feature.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentHomeBinding
import ru.babaetskv.passionwoman.app.permission.Permission
import ru.babaetskv.passionwoman.app.permission.PermissionStatus
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.utils.dialog.DIALOG_ACTIONS_ORIENTATION_VERTICAL
import ru.babaetskv.passionwoman.app.utils.dialog.DialogAction
import ru.babaetskv.passionwoman.app.utils.dialog.showAlertDialog

class HomeFragment : BaseFragment<HomeViewModel, FragmentComponent.NoArgs>() {
    private val binding: FragmentHomeBinding by viewBinding()
    private val homeAdapter: ListDelegationAdapter<List<HomeItem>> by lazy {
        ListDelegationAdapter(
            headerHomeItemAdapterDelegate(viewModel::onHeaderPressed),
            promotionsHomeItemDelegate(viewModel::onPromotionPressed),
            storiesHomeItemDelegate(viewModel::onStoryPressed),
            productsHomeItemDelegate(viewModel::onProductPressed, viewModel::onBuyProductPressed),
            brandsHomeItemDelegate(viewModel::onBrandPressed)
        )
    }
    private lateinit var requestPushPermissionLauncher: ActivityResultLauncher<String>
    private var pushPermissionRationaleDialog: Dialog? = null

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModel<HomeViewModelImpl> {
        parametersOf(requireActivity())
    }
    override val applyBottomInset: Boolean = false
    override val screenName: String = ScreenKeys.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPushPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            viewModel::onPushPermissionRequestResult
        )
    }

    override fun initViews() {
        super.initViews()
        binding.rvHomeItems.run {
            adapter = homeAdapter
            addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_default))
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.homeItemsLiveData.observe(viewLifecycleOwner, ::populateHomeItems)
        viewModel.pushPermissionStatusLiveData.observe(viewLifecycleOwner, ::populatePushPermissionStatus)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is HomeViewModel.OpenLandscapeProductCardEvent -> {
                val fragment = ProductCardFragment.create(event.product.id, isSeparate = false)
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentDetailsContainer, fragment)
                    .commit()
            }
            else -> super.onEvent(event)
        }
    }

    private fun populateHomeItems(items: List<HomeItem>) {
        binding.rvHomeItems.isVisible = items.isNotEmpty()
        homeAdapter.run {
            this.items = items
            notifyDataSetChanged()
        }
    }

    @RequiresApi(33)
    private fun populatePushPermissionStatus(status: PermissionStatus) {
        if (status != PermissionStatus.SHOW_RATIONALE) {
            pushPermissionRationaleDialog?.dismiss()
            pushPermissionRationaleDialog = null
        }
        if (status == PermissionStatus.SHOW_RATIONALE && pushPermissionRationaleDialog?.isShowing != true) {
            showPushPermissionDialog()
        } else if (status == PermissionStatus.REQUEST_PERMISSION) {
            requestPushPermissionLauncher.launch(Permission.PUSH_NOTIFICATION.manifestName)
        }
    }

    private fun showPushPermissionDialog() {
        pushPermissionRationaleDialog = showAlertDialog(
            message = getString(R.string.home_push_rationale_dialog_message),
            actionsOrientation = DIALOG_ACTIONS_ORIENTATION_VERTICAL,
            actions = listOf(
                DialogAction(
                    text = getString(R.string.home_push_rationale_dialog_confirm),
                    isAccent = true,
                    callback = {
                        viewModel.onPushRationaleDialogConfirm()
                    }
                ),
                DialogAction(
                    text = getString(R.string.home_push_rationale_dialog_reject),
                    isAccent = false,
                    callback = {
                        viewModel.onPushRationaleDialogReject()
                    }
                )
            )
        )
    }

    companion object {

        fun create() = HomeFragment()
    }
}
