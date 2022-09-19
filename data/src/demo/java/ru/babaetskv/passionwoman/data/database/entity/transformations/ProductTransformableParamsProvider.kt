package ru.babaetskv.passionwoman.data.database.entity.transformations

import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.api.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity
import ru.babaetskv.passionwoman.data.database.entity.ProductItemEntity
import ru.babaetskv.passionwoman.data.filters.FilterResolver
import ru.babaetskv.passionwoman.data.model.BrandModel
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductItemModel
import ru.babaetskv.passionwoman.domain.AppDispatchers
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList

class ProductTransformableParamsProvider(
    private val database: PassionWomanDatabase,
    private val productItemParamsProvider: ProductItemEntity.TransformableParamsProvider,
    private val dispatchers: AppDispatchers
) : ProductEntity.TransformableParamsProvider {

    override suspend fun provideCategory(categoryId: Int): CategoryModel =
        withContext(dispatchers.IO) {
            return@withContext database.categoryDao.getById(categoryId)
                ?.transform()
                ?: throw ApiExceptionProvider.getNotFoundException("Cannot find category with id $categoryId")
        }

    override suspend fun provideBrand(brandId: Int): BrandModel = withContext(dispatchers.IO) {
        return@withContext database.brandDao.getById(brandId)
            ?.transform()
            ?: throw ApiExceptionProvider.getNotFoundException("Cannot find brand with id $brandId")
    }

    override suspend fun provideProductItems(productId: Int): List<ProductItemModel> =
        withContext(dispatchers.IO) {
            return@withContext database.productItemDao.getByProductId(productId)
                .takeIf { it.isNotEmpty() }
                ?.transformList(productItemParamsProvider)
                ?: throw ApiExceptionProvider.getNotFoundException("Cannot find product items for the product with id $productId")
        }

    override suspend fun provideAdditionalInfo(productId: Int): Map<String, List<String>> =
        withContext(dispatchers.IO) {
            val countries = async(dispatchers.Default) {
                database.productCountryDao.getCodesForProduct(productId)
            }
            val models = async(dispatchers.Default) {
                database.productModelDao.getCodesForProduct(productId)
            }
            val materials = async(dispatchers.Default) {
                database.productMaterialDao.getCodesForProduct(productId) }
            val seasons = async(dispatchers.Default) {
                database.productSeasonDao.getCodesForProduct(productId)
            }
            val styles = async(dispatchers.Default) {
                database.productStyleDao.getCodesForProduct(productId)
            }
            val types = async(dispatchers.Default) {
                database.productTypeDao.getCodesForProduct(productId)
            }
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
