package ru.babaetskv.passionwoman.app.presentation.feature.home

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.*
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.PagedProductsAdapter
import ru.babaetskv.passionwoman.app.utils.dimen
import ru.babaetskv.passionwoman.app.utils.integer
import ru.babaetskv.passionwoman.app.utils.view.LinearLayoutPagerManager
import ru.babaetskv.passionwoman.domain.model.Brand
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Promotion
import ru.babaetskv.passionwoman.domain.model.Story

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

fun storiesHomeItemDelegate(onStoryClickListener: (item: Story) -> Unit) =
    adapterDelegateViewBinding<HomeItem.Stories, HomeItem, ViewItemHomeStoriesBinding>(
        { layoutInflater, parent ->
            ViewItemHomeStoriesBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.root.run {
            layoutManager = LinearLayoutPagerManager(
                context,
                RecyclerView.HORIZONTAL,
                false,
                integer(R.integer.stories_items_per_page),
                dimen(R.dimen.margin_default)
            )
            adapter = StoriesAdapter(onStoryClickListener)
        }
        bind {
            with (binding.root.adapter as StoriesAdapter) {
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
            layoutManager = LinearLayoutPagerManager(
                context,
                RecyclerView.HORIZONTAL,
                false,
                integer(R.integer.products_items_per_page),
                dimen(R.dimen.margin_default)
            )
            adapter = PagedProductsAdapter(onProductClickListener, onBuyProductPressed)
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
        binding.root.adapter = BrandsAdapter(onBrandClickListener)
        bind {
            with (binding.root.adapter as BrandsAdapter) {
                submitList(item.data)
            }
        }
    }
