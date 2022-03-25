package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemCartItemBinding
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setHtmlText
import ru.babaetskv.passionwoman.domain.model.ProductSize

fun productDescriptionItemDelegate() =
    adapterDelegateViewBinding<AddToCartItem.ProductDescription, AddToCartItem, ViewItemCartItemBinding>(
        { layoutInflater, parent ->
            ViewItemCartItemBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        bind {
            val item = this.item.item
            binding.run {
                imgPreview.load(item.preview, R.drawable.photo_placeholder,
                    resizeAsItem = true
                )
                tvTitle.text = item.name
                if (item.price != item.priceWithDiscount) {
                    tvPrice.text = item.priceWithDiscount.toFormattedString()
                    tvPriceDeleted.run {
                        isVisible = true
                        setHtmlText(context.getString(R.string.deleted_text_template, item.price.toFormattedString()))
                    }
                } else {
                    tvPrice.text = item.price.toFormattedString()
                    tvPriceDeleted.isVisible = false
                }
                viewColor.run {
                    val color = item.selectedColor
                    if (color.isMulticolor) {
                        setMulticolor()
                    } else setColor(color.hex.toColorInt())
                    borderVisible = true
                }
                viewSize.btnSize.run {
                    text = item.selectedSize.value
                    isEnabled = true
                    isChecked = true
                    isVisible = item.selectedSize != ProductSize.EMPTY
                }
                layoutCounter.root.visibility = View.INVISIBLE
            }
        }
    }