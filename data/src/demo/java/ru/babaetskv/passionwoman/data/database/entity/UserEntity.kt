package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "surname") val surname: String?,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "avatar") val avatar: String?
) : Transformable<Unit, ProfileModel>() {

    override suspend fun transform(params: Unit): ProfileModel =
        ProfileModel(
            id = id.toString(),
            name = name.orEmpty(),
            surname = surname.orEmpty(),
            phone = phone,
            avatar = avatar
        )
}
