package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ColorEntity::class,
            parentColumns = ["id"],
            childColumns = ["selected_color_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "order_id") val orderId: Long,
    @ColumnInfo(name = "product_id") val productId: Long,
    @ColumnInfo(name = "preview") val preview: String,
    @ColumnInfo(name = "selected_color_id") val selectedColorId: Long,
    @ColumnInfo(name = "selected_size") val selectedSize: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "price_with_discount") val priceWithDiscount: Float,
    @ColumnInfo(name = "count") val count: Int
) : Transformable<CartItemEntity.TransformableParamsProvider, CartItemModel>() {

    override suspend fun transform(params: TransformableParamsProvider): CartItemModel =
        CartItemModel(
            productId = productId,
            preview = preview,
            selectedColor = params.provideColor(selectedColorId),
            selectedSize = selectedSize,
            name = name,
            price = price,
            priceWithDiscount = priceWithDiscount,
            count = count
        )

    interface TransformableParamsProvider {

        suspend fun provideColor(colorId: Long): ColorModel
    }
}
