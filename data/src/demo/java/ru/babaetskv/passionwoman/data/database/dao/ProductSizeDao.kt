package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductSizeEntity

@Dao
interface ProductSizeDao {

    @Query("SELECT * FROM product_sizes")
    suspend fun getAll(): List<ProductSizeEntity>

    @Query("""
        SELECT code
        FROM product_sizes JOIN size_to_product_item ON code = size_to_product_item.product_size_code
        WHERE size_to_product_item.product_item_id = :productItemId
    """)
    suspend fun getForProductItem(productItemId: Long): List<String>

    @Query("""
        SELECT code
        FROM product_sizes JOIN size_to_product_item ON code = size_to_product_item.product_size_code
        WHERE size_to_product_item.product_item_id = :productItemId AND is_available = 1
    """)
    suspend fun getAvailableForProductItem(productItemId: Long): List<String>

    @Insert
    suspend fun insert(vararg entities: ProductSizeEntity)
}
