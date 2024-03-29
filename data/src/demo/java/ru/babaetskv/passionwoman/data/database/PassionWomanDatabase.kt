package ru.babaetskv.passionwoman.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.babaetskv.passionwoman.data.database.dao.*
import ru.babaetskv.passionwoman.data.database.entity.*

@Database(
    entities = [
        ColorEntity::class,
        BrandEntity::class,
        UserEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        ProductTypeEntity::class,
        ProductModelEntity::class,
        ProductSizeEntity::class,
        ProductCountryEntity::class,
        ProductMaterialEntity::class,
        ProductSeasonEntity::class,
        ProductStyleEntity::class,
        ProductItemEntity::class,
        ProductImageEntity::class,
        PromotionEntity::class,
        OrderEntity::class,
        CartItemEntity::class,
        CountryToProductEntity::class,
        ModelToProductEntity::class,
        MaterialToProductEntity::class,
        SeasonToProductEntity::class,
        StyleToProductEntity::class,
        TypeToProductEntity::class,
        SizeToProductItemEntity::class
    ],
    version = 1
)
abstract class PassionWomanDatabase : RoomDatabase() {
    abstract val colorDao: ColorDao
    abstract val brandDao: BrandDao
    abstract val userDao: UserDao
    abstract val categoryDao: CategoryDao
    abstract val productDao: ProductDao
    abstract val productTypeDao: ProductTypeDao
    abstract val productModelDao: ProductModelDao
    abstract val productSizeDao: ProductSizeDao
    abstract val productCountryDao: ProductCountryDao
    abstract val productMaterialDao: ProductMaterialDao
    abstract val productSeasonDao: ProductSeasonDao
    abstract val productStyleDao: ProductStyleDao
    abstract val productItemDao: ProductItemDao
    abstract val productImageDao: ProductImageDao
    abstract val promotionDao: PromotionDao
    abstract val orderDao: OrderDao
    abstract val cartItemDao: CartItemDao
    abstract val countryToProductDao: CountryToProductDao
    abstract val materialToProductDao: MaterialToProductDao
    abstract val modelToProductDao: ModelToProductDao
    abstract val seasonToProductDao: SeasonToProductDao
    abstract val sizeToProductDao: SizeToProductDao
    abstract val typeToProductDao: TypeToProductDao
    abstract val styleToProductDao: StyleToProductDao
}
