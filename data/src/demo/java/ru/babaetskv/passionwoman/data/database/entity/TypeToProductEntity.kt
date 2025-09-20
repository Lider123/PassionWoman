package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "type_to_product",
    foreignKeys = [
        ForeignKey(
            entity = ProductTypeEntity::class,
            parentColumns = ["code"],
            childColumns = ["product_type_code"],
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
data class TypeToProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "product_type_code") val typeCode: String,
    @ColumnInfo(name = "product_id") val productId: Long
)
