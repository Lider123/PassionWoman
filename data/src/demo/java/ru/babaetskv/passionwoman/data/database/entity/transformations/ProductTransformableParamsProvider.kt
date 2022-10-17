package ru.babaetskv.passionwoman.data.database.entity.transformations

import kotlinx.coroutines.Dispatchers
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
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList

class ProductTransformableParamsProvider(
    private val database: PassionWomanDatabase,
    private val productItemParamsProvider: ProductItemEntity.TransformableParamsProvider
) : ProductEntity.TransformableParamsProvider {

    override suspend fun provideCategory(categoryId: Long): CategoryModel =
        withContext(Dispatchers.IO) {
            return@withContext database.categoryDao.getById(categoryId)
                ?.transform()
                ?: throw ApiExceptionProvider.getNotFoundException("Cannot find category with id $categoryId")
        }

    override suspend fun provideBrand(brandId: Long): BrandModel = withContext(Dispatchers.IO) {
        return@withContext database.brandDao.getById(brandId)
            ?.transform()
            ?: throw ApiExceptionProvider.getNotFoundException("Cannot find brand with id $brandId")
    }

    override suspend fun provideProductItems(productId: Long): List<ProductItemModel> =
        withContext(Dispatchers.IO) {
            return@withContext database.productItemDao.getByProductId(productId)
                .takeIf { it.isNotEmpty() }
                ?.transformList(productItemParamsProvider)
                ?: throw ApiExceptionProvider.getNotFoundException("Cannot find product items for the product with id $productId")
        }

    override suspend fun provideAdditionalInfo(productId: Long): Map<String, List<String>> =
        withContext(Dispatchers.IO) {
            val countries = async { database.productCountryDao.getCodesForProduct(productId) }
            val models = async { database.productModelDao.getCodesForProduct(productId) }
            val materials = async { database.productMaterialDao.getCodesForProduct(productId) }
            val seasons = async { database.productSeasonDao.getCodesForProduct(productId) }
            val styles = async { database.productStyleDao.getCodesForProduct(productId) }
            val types = async { database.productTypeDao.getCodesForProduct(productId) }
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
