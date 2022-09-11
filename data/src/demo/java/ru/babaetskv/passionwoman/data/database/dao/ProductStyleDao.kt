package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductStyleEntity

@Dao
interface ProductStyleDao {

    @Query("SELECT * FROM product_styles")
    fun getAll(): List<ProductStyleEntity>

    @Query("""
        SELECT code
        FROM product_styles JOIN style_to_product ON code = style_to_product.product_style_code
        WHERE style_to_product.product_id = :productId
    """)
    suspend fun getCodesForProduct(productId: Int): List<String>
}
