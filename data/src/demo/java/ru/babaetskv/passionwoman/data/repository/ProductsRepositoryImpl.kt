package ru.babaetskv.passionwoman.data.repository

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.*
import ru.babaetskv.passionwoman.data.filters.FilterResolver
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductItem
import ru.babaetskv.passionwoman.domain.repository.ProductsRepository
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalStateException

class ProductsRepositoryImpl(
    private val database: PassionWomanDatabase
) : ProductsRepository {

    override suspend fun saveProduct(product: Product) = withContext(Dispatchers.IO) {
        val productId = insertProduct(product)
        insertProductItems(product.items, productId)
        product.additionalInfo?.let {
            insertProductAdditionalInfo(it, productId)
        }
        return@withContext
    }

    override suspend fun dump() = withContext(Dispatchers.IO) {
        val sd = Environment.getExternalStorageDirectory()
        if (!sd.canWrite()) return@withContext

        val currentDBPath = database.openHelper.writableDatabase.path
        val backupDBPath = "passionwoman.db"
        val currentDB = File(currentDBPath)
        val backupDB = File(sd, backupDBPath)
        if (currentDB.exists()) {
            try {
                val istream = FileInputStream(currentDB).channel
                val ostream = FileOutputStream(backupDB).channel
                ostream.transferFrom(istream, 0, istream.size())
                istream.close()
                ostream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun insertProduct(product: Product): Long {
        val entity = ProductEntity(
            brandId = product.brand?.id,
            categoryId = product.category.id,
            description = product.description,
            name = product.name,
            previewPath = product.preview.url,
            priceWithDiscount = product.priceWithDiscount.toFloat(),
            price = product.price.toFloat(),
            rating = product.rating
        )
        return database.productDao.insert(entity)[0]
    }

    private suspend fun insertProductItems(items: List<ProductItem>, productId: Long) {
        val entities = items.map { item ->
            ProductItemEntity(
                colorId = item.color.id,
                productId = productId
            )
        }
        val itemIds = database.productItemDao.insert(*entities.toTypedArray())
        insertProductSizes(items, itemIds)
        insertProductImages(items, itemIds)
    }

    private suspend fun insertProductSizes(items: List<ProductItem>, itemIds: Array<Long>) {
        val entities = items.flatMapIndexed { i, item ->
            item.sizes.map { size ->
                SizeToProductItemEntity(
                    sizeCode = size.value,
                    productItemId = itemIds[i],
                    isAvailable = size.isAvailable
                )
            }
        }
        database.sizeToProductDao.insert(*entities.toTypedArray())
    }

    private suspend fun insertProductImages(items: List<ProductItem>, itemIds: Array<Long>) {
        val entities: List<ProductImageEntity> = items.flatMapIndexed { i, item ->
            item.images.map {
                ProductImageEntity(
                    imagePath = it.url,
                    productItemId = itemIds[i]
                )
            }
        }
        database.productImageDao.insert(*entities.toTypedArray())
    }

    private suspend fun insertProductAdditionalInfo(
        info: Map<String, List<String>>,
        productId: Long
    ) {
        info.forEach { (code, values) ->
            val resolver = FilterResolver.findByCode(code) ?: return@forEach

            when (resolver) {
                FilterResolver.MODEL -> values.forEach { model ->
                    ModelToProductEntity(
                        modelCode = model,
                        productId = productId
                    ).let {
                        database.modelToProductDao.insert(it)
                    }
                }
                FilterResolver.MATERIAL -> values.forEach { material ->
                    MaterialToProductEntity(
                        materialCode = material,
                        productId = productId
                    ).let {
                        database.materialToProductDao.insert(it)
                    }
                }
                FilterResolver.SEASON -> values.forEach { season ->
                    SeasonToProductEntity(
                        seasonCode = season,
                        productId = productId
                    ).let {
                        database.seasonToProductDao.insert(it)
                    }
                }
                FilterResolver.TYPE -> values.forEach { type ->
                    TypeToProductEntity(
                        typeCode = type,
                        productId = productId
                    ).let {
                        database.typeToProductDao.insert(it)
                    }
                }
                FilterResolver.COUNTRY -> values.forEach { country ->
                    CountryToProductEntity(
                        countryCode = country,
                        productId = productId
                    ).let {
                        database.countryToProductDao.insert(it)
                    }
                }
                FilterResolver.STYLE -> values.forEach { style ->
                    StyleToProductEntity(
                        styleCode = style,
                        productId = productId
                    ).let {
                        database.styleToProductDao.insert(it)
                    }
                }
                else -> {
                    throw IllegalStateException("Values insertion to the database was skipped. Resolver: $resolver")
                }
            }
        }
    }
}
