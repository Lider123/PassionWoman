package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentAddToCartBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseBottomSheetDialogFragment
import ru.babaetskv.passionwoman.domain.model.Product

class AddToCartFragment : BaseBottomSheetDialogFragment<AddToCartViewModel, AddToCartViewModel.Router, AddToCartFragment.Args>() {
    private val binding: FragmentAddToCartBinding by viewBinding()
    private val adapter: ListDelegationAdapter<List<AddToCartItem>> by lazy {
        ListDelegationAdapter(
            productDescriptionItemDelegate(),
            colorsItemDelegate(viewModel::onColorPressed),
            sizesItemDelegate(viewModel::onSizePressed)
        )
    }

    override val layoutRes: Int = R.layout.fragment_add_to_cart
    override val screenName: String = ScreenKeys.NEW_CART_ITEM
    override val viewModel: AddToCartViewModel by viewModel<AddToCartViewModelImpl> {
        parametersOf(args)
    }

    override fun initViews() {
        super.initViews()
        binding.rvItems.adapter = adapter
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.addToCartItemsLiveData.observe(viewLifecycleOwner, ::populateItems)
    }

    private fun populateItems(items: List<AddToCartItem>) {
        adapter.run {
            this.items = items
            notifyDataSetChanged()
        }
    }

    @Parcelize
    data class Args(
        val product: Product
    ) : Parcelable

    companion object {

        fun create(product: Product) = AddToCartFragment().withArgs(Args(product))
    }
}
