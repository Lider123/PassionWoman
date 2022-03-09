package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import android.view.View
import android.view.ViewGroup
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemProductPhotoBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseAdapter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewHolder
import ru.babaetskv.passionwoman.app.presentation.base.EqualDiffUtilCallback
import ru.babaetskv.passionwoman.app.utils.inflateLayout
import ru.babaetskv.passionwoman.app.utils.load

class ProductPhotosAdapter: BaseAdapter<ProductImageItem>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductImageItem> =
        when (viewType) {
            PRODUCT_IMAGE_TYPE -> ProductImageViewHolder(parent.inflateLayout(R.layout.view_item_product_photo))
            else -> EmptyPlaceholderViewHolder(parent.inflateLayout(R.layout.layout_product_card_no_images))
        }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ProductImageItem.ProductImage -> PRODUCT_IMAGE_TYPE
            is ProductImageItem.EmptyPlaceholder -> EMPTY_PLACEHOLDER_TYPE
        }

    class ProductImageViewHolder(v: View) : BaseViewHolder<ProductImageItem>(v) {
        private val binding = ViewItemProductPhotoBinding.bind(v)

        override fun bind(item: ProductImageItem) {
            item as ProductImageItem.ProductImage
            binding.ivPhoto.load(item.data, R.drawable.photo_placeholder)
        }
    }

    class EmptyPlaceholderViewHolder(v: View) : BaseViewHolder<ProductImageItem>(v) {

        override fun bind(item: ProductImageItem) = Unit
    }

    companion object {
        private const val PRODUCT_IMAGE_TYPE = 0
        private const val EMPTY_PLACEHOLDER_TYPE = 1
    }
}