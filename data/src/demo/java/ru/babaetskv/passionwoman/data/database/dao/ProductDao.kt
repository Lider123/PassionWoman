package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getById(productId: Int): ProductEntity?

    @Query("SELECT * FROM products WHERE category_id = :categoryId")
    suspend fun getByCategoryId(categoryId: Int): List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandom(limit: Int): List<ProductEntity>

    @Query("SELECT * FROM products WHERE price_with_discount < price")
    suspend fun getWithDiscount(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id in (:ids)")
    suspend fun getByIds(ids: Collection<Int>): List<ProductEntity>

    @Query("SELECT MIN(price_with_discount) FROM products")
    suspend fun getMinPrice(): Float?

    @Query("SELECT MAX(price_with_discount) FROM products")
    suspend fun getMaxPrice(): Float?

    @Insert
    suspend fun insert(entity: ProductEntity)
}
