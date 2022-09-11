package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.BrandEntity

@Dao
interface BrandDao {

    @Query("SELECT * FROM brands")
    suspend fun getAll(): List<BrandEntity>

    @Query("""
        SELECT brands.*
        FROM brands
        LEFT JOIN (
            SELECT brand_id, COUNT(brand_id) AS frequency
            FROM products
            GROUP BY brand_id
        ) AS frequencies
        ON brands.id = frequencies.brand_id
        ORDER BY frequencies.frequency DESC
        LIMIT :count
    """)
    suspend fun getPopular(count: Int): List<BrandEntity>

    @Query("SELECT * FROM brands WHERE id = :brandId")
    suspend fun getById(brandId: Int): BrandEntity?

    @Insert
    suspend fun insert(entity: BrandEntity)
}
