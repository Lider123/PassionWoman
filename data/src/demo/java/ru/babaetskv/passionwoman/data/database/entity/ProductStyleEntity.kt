package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_styles")
data class ProductStyleEntity(
    @PrimaryKey val code: String,
    @ColumnInfo(name = "ui_name") val uiName: String
)