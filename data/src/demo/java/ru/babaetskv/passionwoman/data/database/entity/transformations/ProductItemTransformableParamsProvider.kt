package ru.babaetskv.passionwoman.data.database.entity.transformations

import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.ProductItemEntity
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform

class ProductItemTransformableParamsProvider(
    private val database: PassionWomanDatabase
) : ProductItemEntity.TransformableParamsProvider {

    override suspend fun provideImages(productItemId: Long): List<String> =
        database.productImageDao.getForProductItem(productItemId)
            .map(AssetDbFormatter::formatAssetDbPath)

    override suspend fun provideColor(colorId: Long): ColorModel =
        database.colorDao.getById(colorId)
            ?.transform()
            ?: throw IllegalStateException("Failed to found color with id $colorId")

    override suspend fun provideSizes(productItemId: Long): List<String>? =
        database.productSizeDao.getForProductItem(productItemId).ifEmpty { null }

    override suspend fun provideAvailableSizes(productItemId: Long): List<String>? =
        database.productSizeDao.getAvailableForProductItem(productItemId).ifEmpty { null }
}
