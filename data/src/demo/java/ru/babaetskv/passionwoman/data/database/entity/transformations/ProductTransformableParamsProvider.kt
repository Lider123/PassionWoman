package ru.babaetskv.passionwoman.data.database.entity.transformations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.api.BaseApiImpl
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity
import ru.babaetskv.passionwoman.data.filters.FilterResolver
import ru.babaetskv.passionwoman.data.model.BrandModel
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductItemModel
import ru.babaetskv.passionwoman.domain.utils.transform
import ru.babaetskv.passionwoman.domain.utils.transformList

class ProductTransformableParamsProvider(
    private val database: PassionWomanDatabase,
    private val api: BaseApiImpl
) : ProductEntity.TransformableParamsProvider {
    private val productItemParamsProvider = ProductItemTransformableParamsProvider(database)

    override suspend fun provideCategory(categoryId: Int): CategoryModel =
        withContext(Dispatchers.IO) {
            return@withContext database.categoryDao.getById(categoryId)
                ?.transform()
                ?: throw api.getInternalServerErrorException("Cannot find category by id $categoryId")
        }

    override suspend fun provideBrand(brandId: Int): BrandModel = withContext(Dispatchers.IO) {
        return@withContext database.brandDao.getById(brandId)
            ?.transform()
            ?: throw api.getInternalServerErrorException("Cannot find brand by id $brandId")
    }

    override suspend fun provideProductItems(productId: Int): List<ProductItemModel> =
        withContext(Dispatchers.IO) {
            return@withContext database.productItemDao.getByProductId(productId)
                .takeIf { it.isNotEmpty() }
                ?.transformList(productItemParamsProvider)
                ?: throw api.getInternalServerErrorException("Cannot find product items for the product with id $productId")
        }

    override suspend fun provideAdditionalInfo(productId: Int): Map<String, List<String>> =
        withContext(Dispatchers.IO) {
            val countries = async { database.productCountryDao.getForProduct(productId) }
            val models = async { database.productModelDao.getForProduct(productId) }
            val materials = async { database.productMaterialDao.getForProduct(productId) }
            val seasons = async { database.productSeasonDao.getForProduct(productId) }
            val styles = async { database.productStyleDao.getForProduct(productId) }
            val types = async { database.productTypeDao.getForProduct(productId) }
            return@withContext mapOf(
                FilterResolver.COUNTRY.code to countries.await(),
                FilterResolver.MODEL.code to models.await(),
                FilterResolver.MATERIAL.code to materials.await(),
                FilterResolver.SEASON.code to seasons.await(),
                FilterResolver.STYLE.code to styles.await(),
                FilterResolver.TYPE.code to types.await()
            ).filterValues(List<String>::isNotEmpty)
        }
}
