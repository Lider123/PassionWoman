package ru.babaetskv.passionwoman.domain.cache

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.domain.cache.base.ListCache
import ru.babaetskv.passionwoman.domain.model.CartItem

class CartItemsInMemoryCache: ListCache<CartItem> {
    override val liveData = MutableLiveData<List<CartItem>>()

    override fun clear() {
        liveData.postValue(emptyList())
    }

    override fun get(): List<CartItem> = liveData.value ?: emptyList()

    override fun add(item: CartItem) {
        val items: MutableList<CartItem> = liveData.value?.toMutableList() ?: mutableListOf()
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
        liveData.postValue(items)
    }

    override fun remove(item: CartItem) {
        val items: MutableList<CartItem> = liveData.value?.toMutableList() ?: mutableListOf()
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
            liveData.postValue(items)
        }
    }

    override fun set(value: List<CartItem>) {
        liveData.postValue(value)
    }
}
