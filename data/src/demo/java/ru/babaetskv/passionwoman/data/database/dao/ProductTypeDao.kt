package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductTypeEntity

@Dao
interface ProductTypeDao {

    @Query("SELECT * FROM product_types")
    suspend fun getAll(): List<ProductTypeEntity>

    @Query("""
        SELECT code
        FROM product_types JOIN type_to_product ON code = type_to_product.product_type_code
        WHERE type_to_product.product_id = :productId
    """)
    suspend fun getForProduct(productId: Int): List<String>
}
