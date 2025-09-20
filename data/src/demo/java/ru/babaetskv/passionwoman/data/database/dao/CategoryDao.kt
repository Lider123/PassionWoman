package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getById(categoryId: Long): CategoryEntity?

    @Insert
    suspend fun insert(entity: CategoryEntity)
}
