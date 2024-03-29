package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductItemEntity

@Dao
interface ProductItemDao {

    @Query("SELECT * FROM product_items WHERE product_id = :productId")
    suspend fun getByProductId(productId: Long): List<ProductItemEntity>

    @Insert
    suspend fun insert(vararg entities: ProductItemEntity): Array<Long>
}
