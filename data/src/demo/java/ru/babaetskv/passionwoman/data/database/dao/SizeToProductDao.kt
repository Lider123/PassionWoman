package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.babaetskv.passionwoman.data.database.entity.SizeToProductItemEntity

@Dao
interface SizeToProductDao {

    @Insert
    fun insert(vararg entities: SizeToProductItemEntity)
}
