package ru.babaetskv.passionwoman.domain.model

data class Cart(
    val items: List<CartItem>,
    val price: Price,
    val total: Price
) {
    val isEmpty: Boolean
        get() = items.isEmpty()

    companion object {
        val EMPTY = Cart(
            items = emptyList(),
            price = Price(0f),
            total = Price(0f)
        )
    }
}
