package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.babaetskv.passionwoman.data.database.entity.CountryToProductEntity

@Dao
interface CountryToProductDao {

    @Insert
    fun insert(entity: CountryToProductEntity)
}
