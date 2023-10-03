package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.CartItemEntity

@Dao
interface CartItemDao {

    @Query("SELECT * FROM cart_items WHERE order_id = :orderId")
    suspend fun getByOrderId(orderId: Long): List<CartItemEntity>

    @Insert
    suspend fun insert(vararg entities: CartItemEntity): Array<Long>
}