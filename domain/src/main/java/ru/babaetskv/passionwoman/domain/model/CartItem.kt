package ru.babaetskv.passionwoman.domain.model

data class CartItem(
    val productId: Int,
    val preview: Image,
    val selectedColor: Color,
    val selectedSize: ProductSize,
    val name: String,
    val price: Price,
    val priceWithDiscount: Price,
    val count: Int
) {

    constructor(product: Product, selectedColor: Color, selectedSize: ProductSize) :
            this(
                productId = product.id,
                preview = product.items
                    .find { it.color == selectedColor }
                    ?.images
                    ?.get(0)
                    ?: product.preview,
                selectedColor = selectedColor,
                selectedSize = selectedSize,
                name = product.name,
                price = product.price,
                priceWithDiscount = product.priceWithDiscount,
                count = 1
            )
}
