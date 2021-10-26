package ru.babaetskv.passionwoman.app.presentation.feature.home

import androidx.lifecycle.LifecycleOwner
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewItemHomeBrandsBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemHomeHeaderBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemHomeProductsBinding
import ru.babaetskv.passionwoman.app.databinding.ViewItemHomePromotionsBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.PagedProductsAdapter
import ru.babaetskv.passionwoman.domain.model.Brand
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Promotion

private const val HOME_PRODUCT_ITEM_WIDTH_RATIO = 0.41f

fun headerHomeItemAdapterDelegate(onClickListener: (item: HomeItem.Header) -> Unit) =
    adapterDelegateViewBinding<HomeItem.Header, HomeItem, ViewItemHomeHeaderBinding>(
        { layoutInflater, parent ->
            ViewItemHomeHeaderBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.root.setOnClickListener {
            onClickListener.invoke(item)
        }
        bind {
            binding.root.run {
                setText(item.titleRes)
                isClickable = item.isClickable
                setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    if (isClickable) R.drawable.ic_forward else 0,
                    0
                )
            }
        }
    }

fun promotionsHomeItemDelegate(onPromotionClickListener: (item: Promotion) -> Unit) =
    adapterDelegateViewBinding<HomeItem.Promotions, HomeItem, ViewItemHomePromotionsBinding>(
        { layoutInflater, parent ->
            ViewItemHomePromotionsBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.run {
            vpPromotions.adapter = PromotionsAdapter(onPromotionClickListener).apply {
                registerAdapterDataObserver(pageIndicatorPromotions.adapterDataObserver)
            }
            pageIndicatorPromotions.setViewPager(vpPromotions)
        }
        bind {
            with (binding.vpPromotions.adapter as PromotionsAdapter) {
                submitList(item.data)
            }
        }
    }

fun LifecycleOwner.productsHomeItemDelegate(
    onProductClickListener: (item: Product) -> Unit,
    onBuyProductPressed: (item: Product) -> Unit
) =
    adapterDelegateViewBinding<HomeItem.Products, HomeItem, ViewItemHomeProductsBinding>(
        { layoutInflater, parent ->
            ViewItemHomeProductsBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.root.run {
            adapter = PagedProductsAdapter(onProductClickListener, onBuyProductPressed,
                itemWidthRatio = HOME_PRODUCT_ITEM_WIDTH_RATIO
            )
            addItemDecoration(EmptyDividerDecoration(context, R.dimen.margin_default))
        }
        bind {
            with (binding.root.adapter as PagedProductsAdapter) {
                submitList(lifecycle, item.data)
            }
        }
    }

fun brandsHomeItemDelegate(onBrandClickListener: (item: Brand) -> Unit) =
    adapterDelegateViewBinding<HomeItem.Brands, HomeItem, ViewItemHomeBrandsBinding>(
        { layoutInflater, parent ->
            ViewItemHomeBrandsBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.root.run {
            adapter = BrandsAdapter(onBrandClickListener)
            addItemDecoration(EmptyDividerDecoration(context, R.dimen.margin_small))
        }
        bind {
            with (binding.root.adapter as BrandsAdapter) {
                submitList(item.data)
            }
        }
    }
