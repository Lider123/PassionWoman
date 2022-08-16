package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.model.PromotionModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(tableName = "promotions")
data class PromotionEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "image_path") val imagePath: String
) : Transformable<Unit, PromotionModel>() {

    override suspend fun transform(params: Unit): PromotionModel =
        PromotionModel(
            id = id.toString(),
            banner = imagePath
        )
}
