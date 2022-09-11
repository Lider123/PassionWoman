package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.data.model.ProductItemModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(
    tableName = "product_items",
    foreignKeys = [
        ForeignKey(
            entity = ColorEntity::class,
            parentColumns = ["id"],
            childColumns = ["color_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductItemEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "color_id") val colorId: Int,
    @ColumnInfo(name = "product_id") val productId: Int
) : Transformable<ProductItemEntity.TransformableParamsProvider, ProductItemModel>() {

    override suspend fun transform(params: TransformableParamsProvider): ProductItemModel =
        ProductItemModel(
            color = params.provideColor(colorId),
            sizes = params.provideSizes(id),
            availableSizes = params.provideAvailableSizes(id),
            images = params.provideImages(id)
        )

    interface TransformableParamsProvider {
        suspend fun provideColor(colorId: Int): ColorModel
        suspend fun provideSizes(productItemId: Int): List<String>?
        suspend fun provideAvailableSizes(productItemId: Int): List<String>?
        suspend fun provideImages(productItemId: Int): List<String>
    }
}
