package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.data.model.OrderModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "status") val status: String
) : Transformable<OrderEntity.TransformableParamsProvider, OrderModel>() {

    override suspend fun transform(params: TransformableParamsProvider): OrderModel =
        OrderModel(
            id = id,
            createdAt = createdAt,
            cartItems = params.provideCartItems(id),
            status = status
        )

    interface TransformableParamsProvider {

        suspend fun provideCartItems(orderId: Long): List<CartItemModel>
    }
}
