package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.PromotionEntity

@Dao
interface PromotionDao {

    @Query("SELECT * FROM promotions")
    fun getAll(): List<PromotionEntity>
}
