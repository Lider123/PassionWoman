package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.babaetskv.passionwoman.data.database.entity.StyleToProductEntity

@Dao
interface StyleToProductDao {

    @Insert
    suspend fun insert(vararg entities: StyleToProductEntity)
}