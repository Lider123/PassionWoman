package ru.babaetskv.passionwoman.data.database.entity.transformations

import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.ProductItemEntity
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.domain.utils.transform

class ProductItemTransformableParamsProvider(
    private val database: PassionWomanDatabase
) : ProductItemEntity.TransformableParamsProvider {

    override suspend fun provideImages(productItemId: Int): List<String> =
        database.productImageDao.getForProductItem(productItemId)

    override suspend fun provideColor(colorId: Int): ColorModel =
        database.colorDao.getById(colorId)
            ?.transform()
            ?: throw IllegalStateException("Failed to found color with id $colorId")

    override suspend fun provideSizes(productItemId: Int): List<String>? =
        database.productSizeDao.getForProductItem(productItemId).ifEmpty { null }

    override suspend fun provideAvailableSizes(productItemId: Int): List<String>? =
        database.productSizeDao.getAvailableForProductItem(productItemId).ifEmpty { null }
}