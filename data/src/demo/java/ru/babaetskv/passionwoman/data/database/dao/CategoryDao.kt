package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAll(): List<CategoryEntity>
}
