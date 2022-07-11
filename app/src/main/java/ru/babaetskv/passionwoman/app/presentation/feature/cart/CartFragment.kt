package ru.babaetskv.passionwoman.app.presentation.feature.cart

import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentCartBinding
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.utils.setHtmlText
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.Price

class CartFragment : BaseFragment<CartViewModel, CartViewModel.Router, FragmentComponent.NoArgs>() {
    private val binding: FragmentCartBinding by viewBinding()
    private val adapter: CartItemsAdapter by lazy {
        CartItemsAdapter(viewModel::onAddCartItemPressed, viewModel::onRemoveCartItemPressed)
    }

    override val layoutRes: Int = R.layout.fragment_cart
    override val screenName: String = ScreenKeys.CART
    override val viewModel: CartViewModel by viewModel<CartViewModelImpl>()
    override val applyBottomInset: Boolean = false

    override fun initViews() {
        // TODO: fix loading view here and everywhere else
        super.initViews()
        binding.run {
            rvCartItems.adapter = adapter
            btnCheckout.setOnSingleClickListener {
                viewModel.onCheckoutPressed()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.cartItemsLiveData.observe(viewLifecycleOwner, {
            populateCartItems(it)
            populateTotal(it)
        })
    }

    override fun handleRouterEvent(event: CartViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            is CartViewModel.Router.Orders -> router.navigateTo(Screens.orders())
        }
    }

    private fun populateCartItems(items: List<CartItem>) {
        adapter.submitData(items)
        binding.rvCartItems.isVisible = items.isNotEmpty()
    }

    private fun populateTotal(items: List<CartItem>) {
        binding.run {
            val total: Price = items.map { it.price * it.count }
                .reduceOrNull { acc, price -> acc + price } ?: Price()
            val totalWithDiscount: Price = items.map { it.priceWithDiscount * it.count }
                .reduceOrNull { acc, price -> acc + price } ?: Price()
            if (total != totalWithDiscount) {
                tvPrice.text = totalWithDiscount.toFormattedString()
                tvPriceDeleted.run {
                    isVisible = true
                    setHtmlText(context.getString(R.string.deleted_text_template, total.toFormattedString()))
                }
            } else {
                tvPrice.text = total.toFormattedString()
                tvPriceDeleted.isVisible = false
            }
        }
    }

    companion object {

        fun create() = CartFragment()
    }
}
