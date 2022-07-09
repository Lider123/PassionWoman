package ru.babaetskv.passionwoman.app.presentation.feature.orderlist

import android.viewbinding.library.fragment.viewBinding
import androidx.core.os.bundleOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentOrderListBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.presentation.view.ToolbarView
import ru.babaetskv.passionwoman.domain.model.Order

class OrderListFragment : BaseFragment<OrderListViewModel, OrderListViewModel.Router, FragmentComponent.NoArgs>() {
    private val binding: FragmentOrderListBinding by viewBinding()
    private val ordersAdapter: OrdersAdapter by lazy {
        OrdersAdapter(viewModel::onOrderPressed)
    }

    override val layoutRes: Int = R.layout.fragment_order_list
    override val screenName: String = ScreenKeys.ORDER_LIST
    override val viewModel: OrderListViewModel by viewModel<OrderListViewModelImpl>()

    override fun initViews() {
        super.initViews()
        binding.run {
            toolbar.setStartActions(
                ToolbarView.Action(R.drawable.ic_back) {
                    viewModel.onBackPressed()
                }
            )
            rvOrders.adapter = ordersAdapter
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.ordersLiveData.observe(viewLifecycleOwner, ::populateOrders)
    }

    private fun populateOrders(orders: List<Order>) {
        ordersAdapter.submitData(orders)
    }

    companion object {

        fun create() = OrderListFragment().apply {
            arguments = bundleOf()
        }
    }
}
