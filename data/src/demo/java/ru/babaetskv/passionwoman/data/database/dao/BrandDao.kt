package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.BrandEntity

@Dao
interface BrandDao {

    @Query("SELECT * FROM brands LIMIT :count")
    suspend fun getPopularBrands(count: Int): List<BrandEntity>
}
