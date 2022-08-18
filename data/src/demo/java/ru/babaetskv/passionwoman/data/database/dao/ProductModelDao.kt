package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductModelEntity

@Dao
interface ProductModelDao {

    @Query("SELECT * FROM product_models")
    suspend fun getAll(): List<ProductModelEntity>

    @Query("""
        SELECT code
        FROM product_models JOIN model_to_product ON code = model_to_product.product_model_code
        WHERE model_to_product.product_id = :productId
    """)
    suspend fun getForProduct(productId: Int): List<String>

    @Insert
    suspend fun insert(vararg entities: ProductModelEntity)
}
