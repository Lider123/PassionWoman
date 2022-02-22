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
import ru.babaetskv.passionwoman.domain.model.Image

class ProductPhotosAdapter: BaseAdapter<Image>(EqualDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Image> =
        ViewHolder(parent.inflateLayout(R.layout.view_item_product_photo))

    class ViewHolder(v: View) : BaseViewHolder<Image>(v) {
        private val binding = ViewItemProductPhotoBinding.bind(v)

        override fun bind(item: Image) {
            binding.ivPhoto.load(item, R.drawable.photo_placeholder)
        }
    }
}