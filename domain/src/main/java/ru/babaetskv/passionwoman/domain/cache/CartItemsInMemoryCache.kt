package ru.babaetskv.passionwoman.domain.cache

import kotlinx.coroutines.flow.MutableStateFlow
import ru.babaetskv.passionwoman.domain.cache.base.ListCache
import ru.babaetskv.passionwoman.domain.model.CartItem

class CartItemsInMemoryCache: ListCache<CartItem> {
    override val flow = MutableStateFlow<List<CartItem>>(emptyList())

    override suspend fun clear() {
        flow.emit(emptyList())
    }

    override suspend fun get(): List<CartItem> = flow.value

    override suspend fun add(item: CartItem) {
        val items: MutableList<CartItem> = flow.value.toMutableList()
        val existingItem = items.find {
            it.productId == item.productId
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
        flow.emit(items)
    }

    override suspend fun remove(item: CartItem) {
        val items: MutableList<CartItem> = flow.value.toMutableList()
        val existingItem = items.find {
            it.productId == item.productId
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
            flow.emit(items)
        }
    }

    override suspend fun set(value: List<CartItem>) {
        flow.emit(value)
    }
}
