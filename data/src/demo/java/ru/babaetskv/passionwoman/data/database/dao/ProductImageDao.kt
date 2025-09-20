package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductImageEntity

@Dao
interface ProductImageDao {

    @Query("SELECT image_path FROM product_images WHERE product_item_id = :productItemId")
    suspend fun getForProductItem(productItemId: Long): List<String>

    @Insert
    suspend fun insert(vararg entity: ProductImageEntity): Array<Long>
}
