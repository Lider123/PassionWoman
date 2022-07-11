package ru.babaetskv.passionwoman.app.presentation.feature.orderlist

import android.graphics.Color
import android.view.ViewGroup
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemOrderBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductPreviewBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductPreviewMoreBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.DiffUtilAdapter
import ru.babaetskv.passionwoman.app.utils.datetime.DefaultDateTimeConverter
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.order.DefaultOrderStatusResourcesResolverProvider
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.viewBinding
import ru.babaetskv.passionwoman.domain.model.Order

class OrdersAdapter(
    private val onItemClick: (Order) -> Unit
) : DiffUtilAdapter<Order>() {

    override fun createDiffUtilCallback(
        oldList: List<Order>,
        newList: List<Order>
    ): Callback<Order> = OrderDiffUtilCallback(oldList, newList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Order> =
        parent.viewBinding(ViewItemOrderBinding::inflate).let {
            ViewHolder(it, onItemClick)
        }

    class ViewHolder(
        private val binding: ViewItemOrderBinding,
        onItemClick: (Order) -> Unit
    ) : BaseViewHolder<Order>(binding.root) {
        private var item: Order? = null

        init {
            binding.run {
                root.setOnSingleClickListener {
                    item?.let(onItemClick)
                }
            }
        }

        override fun bind(item: Order) {
            this.item = item
            binding.run {
                tvTitle.text = context.getString(R.string.order_title_placeholder, item.id)
                tvDatetime.text = item.formatCreatedAt(DefaultDateTimeConverter)
                tvStatus.run {
                    val resolver = DefaultOrderStatusResourcesResolverProvider.provide(context)
                    text = item.status?.getTitle(resolver).orEmpty()
                    setTextColor(item.status?.getColor(resolver) ?: Color.BLACK)
                }
                layoutPreviews.run {
                    removeAllViews()
                    item.cartItems.take(PREVIEWS_MAX_COUNT).forEach { cartItem ->
                        viewBinding(ViewItemProductPreviewBinding::inflate).apply {
                            ivPreview.load(cartItem.preview, R.drawable.photo_placeholder,
                                resizeAsItem = true
                            )
                        }.root.let(::addView)
                    }
                    if (item.cartItems.size > PREVIEWS_MAX_COUNT) {
                        viewBinding(ViewItemProductPreviewMoreBinding::inflate).apply {
                            tvCounter.text = context.getString(
                                R.string.order_previews_more_placeholder,
                                item.cartItems.size - PREVIEWS_MAX_COUNT
                            )
                        }.root.let(::addView)
                    }
                }
            }
        }
    }

    class OrderDiffUtilCallback(
        oldList: List<Order>,
        newList: List<Order>
    ) : DiffUtilAdapter.Callback<Order>(oldList, newList) {

        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem == newItem
    }

    companion object {
        private const val PREVIEWS_MAX_COUNT = 3
    }
}
