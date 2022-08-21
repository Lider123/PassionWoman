package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductSeasonEntity

@Dao
interface ProductSeasonDao {

    @Query("SELECT * FROM product_seasons")
    suspend fun getAll(): List<ProductSeasonEntity>

    @Query("""
        SELECT code
        FROM product_seasons JOIN season_to_product ON code = season_to_product.product_season_code
        WHERE season_to_product.product_id = :productId
    """)
    suspend fun getCodesForProduct(productId: Int): List<String>
}
