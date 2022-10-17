package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "size_to_product_item",
    foreignKeys = [
        ForeignKey(
            entity = ProductSizeEntity::class,
            parentColumns = ["code"],
            childColumns = ["product_size_code"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_item_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SizeToProductItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "product_size_code") val sizeCode: String,
    @ColumnInfo(name = "product_item_id") val productItemId: Long,
    @ColumnInfo(name = "is_available") val isAvailable: Boolean
)
