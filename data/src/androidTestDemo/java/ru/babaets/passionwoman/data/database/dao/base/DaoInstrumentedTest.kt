package ru.babaets.passionwoman.data.database.dao.base

import ru.babaetskv.passionwoman.data.database.entity.*

abstract class DaoInstrumentedTest {

    protected fun createBrand(id: Long) =
        BrandEntity(
            id = id,
            name = "Brand $id",
            logoPath = "brand_${id}_logo_path",
            createdAt = 0
        )

    protected fun createProduct(
        id: Long,
        categoryId: Long,
        price: Float = 1f,
        priceWithDiscount: Float = 1f,
        brandId: Long? = null) =
        ProductEntity(
            id = id,
            categoryId = categoryId,
            name = "Product $id",
            previewPath = "product_${id}_preview_path",
            price = price,
            priceWithDiscount = priceWithDiscount,
            rating = 0f,
            description = null,
            brandId = brandId,
            createdAt = 0
        )

    protected fun createCategory(id: Long) =
        CategoryEntity(
            id = id,
            name = "Category $id",
            imagePath = "category_${id}_image_path"
        )

    protected fun createColor(id: Long) =
        ColorEntity(
            id = id,
            uiName = "Color $id",
            hex = "hex$id"
        )

    protected fun createCountry(id: Int) =
        ProductCountryEntity(
            code = "country$id",
            uiName = "Country $id"
        )

    protected fun createProductImage(id: Long, productItemId: Long) =
        ProductImageEntity(
            id = id,
            imagePath = "product_image_${id}_path",
            productItemId = productItemId
        )

    protected fun createProductItem(id: Long, productId: Long, colorId: Long) =
        ProductItemEntity(
            id = id,
            productId = productId,
            colorId = colorId
        )

    protected fun createUser(id: Long) =
        UserEntity(
            id = id,
            name = "Jane",
            surname = "Doe",
            phone = "9100000000",
            avatar = null
        )

    protected fun createProductMaterial(id: Int) =
        ProductMaterialEntity(
            code = "material$id",
            uiName = "Material $id"
        )

    protected fun createMaterialToProductEntity(id: Long, materialId: Int, productId: Long) =
        MaterialToProductEntity(
            id = id,
            materialCode = "material$materialId",
            productId = productId
        )

    protected fun createProductModel(id: Int) =
        ProductModelEntity(
            code = "model$id",
            uiName = "Model $id"
        )

    protected fun createModelToProductEntity(id: Long, modelId: Int, productId: Long) =
        ModelToProductEntity(
            id = id,
            modelCode = "model$modelId",
            productId = productId
        )

    protected fun createProductSeason(id: Int) =
        ProductSeasonEntity(
            code = "season$id",
            uiName = "Season $id"
        )

    protected fun createSeasonToProductEntity(id: Long, seasonId: Int, productId: Long) =
        SeasonToProductEntity(
            id = id,
            seasonCode = "season$seasonId",
            productId = productId
        )

    protected fun createProductSize(id: Int) =
        ProductSizeEntity(
            code = "size$id",
            uiName = "size $id"
        )

    protected fun createSizeToProductItemEntity(
        id: Long,
        sizeCode: String,
        productItemId: Long,
        isAvailable: Boolean = true
    ) =
        SizeToProductItemEntity(
            id = id,
            sizeCode = sizeCode,
            productItemId = productItemId,
            isAvailable = isAvailable
        )
}
