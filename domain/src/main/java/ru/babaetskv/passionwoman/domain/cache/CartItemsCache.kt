package ru.babaetskv.passionwoman.domain.cache

import ru.babaetskv.passionwoman.domain.cache.base.ListCache
import ru.babaetskv.passionwoman.domain.model.CartItem

class CartItemsInMemoryCache: ListCache<CartItem> {
    val items = mutableListOf<CartItem>()

    override fun clear() {
        items.clear()
    }

    override fun get(): List<CartItem> = items

    override fun add(item: CartItem) {
        val existingItem = items.find {
            it.product.id == item.product.id
                    && it.selectedSize == item.selectedSize
                    && it.selectedColor == item.selectedColor
        }
        existingItem?.let {
            val position = items.indexOf(it)
            items.remove(it)
            items.add(position, it.copy(
                count = it.count + item.count
            ))
        } ?: items.add(item)
    }

    override fun remove(item: CartItem) {
        val existingItem = items.find {
            it.product.id == item.product.id
                    && it.selectedSize == item.selectedSize
                    && it.selectedColor == item.selectedColor
        }
        existingItem?.let {
            val position = items.indexOf(it)
            items.remove(it)
            val remainingCount = it.count - item.count
            if (remainingCount > 0) {
                items.add(position, it.copy(
                    count = remainingCount
                ))
            }
        }
    }

    override fun set(value: List<CartItem>) {
        items.clear()
        items.addAll(value)
    }
}
