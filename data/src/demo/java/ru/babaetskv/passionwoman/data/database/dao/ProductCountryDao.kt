package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductCountryEntity

@Dao
interface ProductCountryDao {

    @Query("SELECT * FROM product_countries")
    suspend fun getAll(): List<ProductCountryEntity>

    @Query("""
        SELECT code
        FROM product_countries JOIN country_to_product ON code = country_to_product.product_country_code
        WHERE country_to_product.product_id = :productId
    """)
    suspend fun getForProduct(productId: Int): List<String>
}
