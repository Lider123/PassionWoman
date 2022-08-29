package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.database.entity.transformations.AssetDbFormatter
import ru.babaetskv.passionwoman.data.model.BrandModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "logo_path") val logoPath: String,
    @ColumnInfo(name = "name") val name: String
) : Transformable<Unit, BrandModel>() {

    override suspend fun transform(params: Unit): BrandModel =
        BrandModel(
            id = id,
            logoPath = AssetDbFormatter.formatAssetDbPath(logoPath),
            name = name
        )
}
