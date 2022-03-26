package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemAddToCartItemColorsBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemAddToCartItemConfirmationBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemAddToCartItemSizesBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemCartItemBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ColorsAdapter
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductSizesAdapter
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setHtmlText
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

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

fun colorsItemDelegate(onColorClick: (SelectableItem<Color>) -> Unit) =
    adapterDelegateViewBinding<AddToCartItem.Colors, AddToCartItem, ViewItemAddToCartItemColorsBinding>(
        { layoutInflater, parent ->
            ViewItemAddToCartItemColorsBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.root.run {
            addItemDecoration(EmptyDividerDecoration(context, R.dimen.margin_small))
            adapter = ColorsAdapter(onColorClick)
        }
        bind {
            val colors = item.colors
            with (binding.root.adapter as? ColorsAdapter) {
                this?.submitList(colors)
            }
        }
    }

fun sizesItemDelegate(onSizeClick: (SelectableItem<ProductSize>) -> Unit) =
    adapterDelegateViewBinding<AddToCartItem.Sizes, AddToCartItem, ViewItemAddToCartItemSizesBinding>(
        { layoutInflater, parent ->
            ViewItemAddToCartItemSizesBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.root.run {
            itemAnimator = null
            addItemDecoration(EmptyDividerDecoration(context, R.dimen.margin_extra_small))
            adapter = ProductSizesAdapter(onSizeClick)
        }
        bind {
            val sizes = item.sizes
            with (binding.root.adapter as? ProductSizesAdapter) {
                this?.submitList(sizes)
            }
        }
    }

fun confirmationItemDelegate(onConfirmClick: () -> Unit) =
    adapterDelegateViewBinding<AddToCartItem.Confirmation, AddToCartItem, ViewItemAddToCartItemConfirmationBinding>(
        { layoutInflater, parent ->
            ViewItemAddToCartItemConfirmationBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.btnConfirm.setOnSingleClickListener {
            onConfirmClick.invoke()
        }
        bind {
            binding.btnConfirm.run {
                isEnabled = item.isEnabled
                setText(if (item.isEnabled) R.string.product_card_add_to_cart else R.string.product_card_not_available)
            }
        }
    }