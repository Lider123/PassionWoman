package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.babaetskv.passionwoman.data.database.entity.TypeToProductEntity

@Dao
interface TypeToProductDao {

    @Insert
    suspend fun insert(vararg entities: TypeToProductEntity)
}
