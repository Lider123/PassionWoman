package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_images",
    foreignKeys = [
        ForeignKey(
            entity = ProductItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_item_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "image_path") val imagePath: String,
    @ColumnInfo(name = "product_item_id") val productItemId: Long
)
