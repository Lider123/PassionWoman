package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProductImageDao {

    @Query("SELECT image_path FROM product_images WHERE product_item_id = :productItemId")
    fun getForProductItem(productItemId: Int): List<String>
}
