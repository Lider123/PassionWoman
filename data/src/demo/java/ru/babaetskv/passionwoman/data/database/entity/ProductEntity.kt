package ru.babaetskv.passionwoman.data.database.entity

import androidx.room.*
import ru.babaetskv.passionwoman.data.database.entity.transformations.AssetDbFormatter
import ru.babaetskv.passionwoman.data.model.BrandModel
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductItemModel
import ru.babaetskv.passionwoman.data.model.ProductModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BrandEntity::class,
            parentColumns = ["id"],
            childColumns = ["brand_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "preview_path") val previewPath: String,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "price_with_discount") val priceWithDiscount: Float,
    @ColumnInfo(name = "rating") val rating: Float,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "brand_id") val brandId: Long?
) : Transformable<ProductEntity.TransformableParamsProvider, ProductModel>() {

    override suspend fun transform(params: TransformableParamsProvider): ProductModel =
        ProductModel(
            id = id,
            category = params.provideCategory(categoryId),
            name = name,
            description = description,
            preview = AssetDbFormatter.formatAssetDbPath(previewPath),
            price = price,
            priceWithDiscount = priceWithDiscount,
            rating = rating,
            brand = brandId?.let {
                 params.provideBrand(it)
            },
            additionalInfo = params.provideAdditionalInfo(id),
            createdAt = createdAt,
            items = params.provideProductItems(id)
        )

    interface TransformableParamsProvider {
        suspend fun provideCategory(categoryId: Long): CategoryModel
        suspend fun provideBrand(brandId: Long): BrandModel
        suspend fun provideProductItems(productId: Long): List<ProductItemModel>
        suspend fun provideAdditionalInfo(productId: Long): Map<String, List<String>>
    }
}
