package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "material_to_product",
    foreignKeys = [
        ForeignKey(
            entity = ProductMaterialEntity::class,
            parentColumns = ["code"],
            childColumns = ["product_material_code"],
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
data class MaterialToProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "product_material_code") val materialCode: String,
    @ColumnInfo(name = "product_id") val productId: Long
)
