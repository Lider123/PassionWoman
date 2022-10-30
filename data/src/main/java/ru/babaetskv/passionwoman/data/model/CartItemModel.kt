package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class CartItemModel(
    @Json(name = "productId") val productId: Long,
    @Json(name = "preview") val preview: String,
    @Json(name = "selectedColor") val selectedColor: ColorModel,
    @Json(name = "selectedSize") val selectedSize: String,
    @Json(name = "name") val name: String,
    @Json(name = "price") val price: Float,
    @Json(name = "priceWithDiscount") val priceWithDiscount: Float,
    @Json(name = "count") val count: Int
) : Transformable<Unit, CartItem>() {

    constructor(item: CartItem) :
        this(
            productId = item.productId,
            preview = item.preview.url,
            selectedColor = ColorModel(item.selectedColor),
            selectedSize = item.selectedSize.value,
            name = item.name,
            price = item.price.toFloat(),
            priceWithDiscount = item.priceWithDiscount.toFloat(),
            count = item.count
        )

    override suspend fun transform(params: Unit): CartItem =
        CartItem(
            productId = productId,
            preview = Image(preview),
            selectedColor = selectedColor.transform(),
            selectedSize = ProductSize(selectedSize, true),
            name = name,
            price = Price(price),
            priceWithDiscount = Price(priceWithDiscount),
            count = count
        )
}
