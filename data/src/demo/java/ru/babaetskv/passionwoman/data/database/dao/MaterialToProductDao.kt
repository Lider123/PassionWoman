package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.babaetskv.passionwoman.data.database.entity.MaterialToProductEntity

@Dao
interface MaterialToProductDao {

    @Insert
    fun insert(vararg entities: MaterialToProductEntity)
}
