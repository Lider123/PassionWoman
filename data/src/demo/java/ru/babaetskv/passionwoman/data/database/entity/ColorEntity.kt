package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colors")
data class ColorEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "ui_name") val uiName: String,
    @ColumnInfo(name = "hex") val hex: String
)
