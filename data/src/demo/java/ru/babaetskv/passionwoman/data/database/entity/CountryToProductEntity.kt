package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "country_to_product",
    foreignKeys = [
        ForeignKey(
            entity = ProductCountryEntity::class,
            parentColumns = ["code"],
            childColumns = ["product_country_code"],
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
data class CountryToProductEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "product_country_code") val countryCode: String,
    @ColumnInfo(name = "product_id") val productId: Int
)
