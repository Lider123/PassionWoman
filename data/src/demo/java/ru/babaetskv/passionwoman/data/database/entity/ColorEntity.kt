package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(tableName = "colors")
data class ColorEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "ui_name") val uiName: String,
    @ColumnInfo(name = "hex") val hex: String
) : Transformable<Unit, ColorModel>() {

    override suspend fun transform(params: Unit): ColorModel =
        ColorModel(
            id = id,
            uiName = uiName,
            hex = hex
        )
}
