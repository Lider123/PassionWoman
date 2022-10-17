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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "color_id") val colorId: Long,
    @ColumnInfo(name = "product_id") val productId: Long
) : Transformable<ProductItemEntity.TransformableParamsProvider, ProductItemModel>() {

    override suspend fun transform(params: TransformableParamsProvider): ProductItemModel =
        ProductItemModel(
            color = params.provideColor(colorId),
            sizes = params.provideSizes(id),
            availableSizes = params.provideAvailableSizes(id),
            images = params.provideImages(id)
        )

    interface TransformableParamsProvider {
        suspend fun provideColor(colorId: Long): ColorModel
        suspend fun provideSizes(productItemId: Long): List<String>?
        suspend fun provideAvailableSizes(productItemId: Long): List<String>?
        suspend fun provideImages(productItemId: Long): List<String>
    }
}
