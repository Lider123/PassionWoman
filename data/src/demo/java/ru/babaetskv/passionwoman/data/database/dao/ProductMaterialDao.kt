package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.ProductMaterialEntity

@Dao
interface ProductMaterialDao {

    @Query("SELECT * FROM product_materials")
    suspend fun getAll(): List<ProductMaterialEntity>

    @Query("""
        SELECT code
        FROM product_materials JOIN material_to_product ON code = material_to_product.product_material_code
        WHERE material_to_product.product_id = :productId
    """)
    suspend fun getForProduct(productId: Int): List<String>

    @Insert
    suspend fun insert(vararg entities: ProductMaterialEntity)
}
