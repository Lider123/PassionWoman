package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "style_to_product",
    foreignKeys = [
        ForeignKey(
            entity = ProductStyleEntity::class,
            parentColumns = ["code"],
            childColumns = ["product_style_code"],
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
data class StyleToProductEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "product_style_code") val styleCode: String,
    @ColumnInfo(name = "product_id") val productId: Int
)
