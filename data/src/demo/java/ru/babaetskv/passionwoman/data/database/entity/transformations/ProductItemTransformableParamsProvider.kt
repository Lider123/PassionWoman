package ru.babaetskv.passionwoman.data.database.entity.transformations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.ProductItemEntity
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform

class ProductItemTransformableParamsProvider(
    private val database: PassionWomanDatabase
) : ProductItemEntity.TransformableParamsProvider {

    override suspend fun provideImages(productItemId: Long): List<String> =
        withContext(Dispatchers.IO) {
            return@withContext database.productImageDao.getForProductItem(productItemId)
                .map(AssetDbFormatter::formatAssetDbPath)
        }

    override suspend fun provideColor(colorId: Long): ColorModel = withContext(Dispatchers.IO) {
        return@withContext database.colorDao.getById(colorId)
            ?.transform()
            ?: throw IllegalStateException("Failed to found color with id $colorId")
    }

    override suspend fun provideSizes(productItemId: Long): List<String>? =
        withContext(Dispatchers.IO) {
            return@withContext database.productSizeDao.getForProductItem(productItemId)
                .ifEmpty { null }
        }

    override suspend fun provideAvailableSizes(productItemId: Long): List<String>? =
        withContext(Dispatchers.IO) {
            return@withContext database.productSizeDao.getAvailableForProductItem(productItemId)
                .ifEmpty { null }
        }
}
