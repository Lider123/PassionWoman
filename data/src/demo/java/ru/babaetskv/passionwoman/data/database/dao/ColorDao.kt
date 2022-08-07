package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ColorEntity

@Dao
interface ColorDao {

    @Query("SELECT * FROM colors")
    suspend fun getAll(): List<ColorEntity>

    @Query("SELECT * FROM colors WHERE id = :colorId")
    suspend fun getById(colorId: Int): ColorEntity?

    @Insert
    suspend fun insert(entity: ColorEntity)
}
