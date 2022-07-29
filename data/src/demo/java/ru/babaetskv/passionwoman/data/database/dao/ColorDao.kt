package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ColorEntity

@Dao
interface ColorDao {

    @Query("SELECT * FROM colors")
    suspend fun getAll(): List<ColorEntity>
}
