package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.babaetskv.passionwoman.data.database.entity.ModelToProductEntity

@Dao
interface ModelToProductDao {

    @Insert
    fun insert(vararg entities: ModelToProductEntity)
}
