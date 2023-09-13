package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.babaetskv.passionwoman.data.database.entity.OrderEntity

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders")
    suspend fun getAll(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getById(orderId: Long): OrderEntity?

    @Insert
    suspend fun insert(vararg entities: OrderEntity): Array<Long>

    @Update
    suspend fun update(entity: OrderEntity)
}
