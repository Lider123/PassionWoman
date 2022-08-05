package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.model.ProductModel

@Entity(
    tableName = "model_to_product",
    foreignKeys = [
        ForeignKey(
            entity = ProductModelEntity::class,
            parentColumns = ["code"],
            childColumns = ["product_model_code"],
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
data class ModelToProductEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "product_model_code") val modelCode: String,
    @ColumnInfo(name = "product_id") val productId: Int
)
