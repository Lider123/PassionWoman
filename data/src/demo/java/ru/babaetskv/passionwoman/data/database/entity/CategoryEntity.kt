package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.database.entity.transformations.AssetDbFormatter
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_path") val imagePath: String
) : Transformable<Unit, CategoryModel>() {

    override suspend fun transform(params: Unit): CategoryModel =
        CategoryModel(
            id = id,
            name = name,
            image = AssetDbFormatter.formatAssetDbPath(imagePath)
        )
}
