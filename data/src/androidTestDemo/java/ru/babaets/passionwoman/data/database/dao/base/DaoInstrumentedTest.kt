package ru.babaets.passionwoman.data.database.dao.base

import ru.babaetskv.passionwoman.data.database.entity.*

abstract class DaoInstrumentedTest {

    protected fun createBrand(id: Int) =
        BrandEntity(
            id = id,
            name = "Brand $id",
            logoPath = "brand_${id}_logo_path"
        )

    protected fun createProduct(
        id: Int,
        categoryId: Int,
        price: Float = 1f,
        priceWithDiscount: Float = 1f,
        brandId: Int? = null) =
        ProductEntity(
            id = id,
            categoryId = categoryId,
            name = "Product $id",
            previewPath = "product_${id}_preview_path",
            price = price,
            priceWithDiscount = priceWithDiscount,
            rating = 0f,
            description = null,
            brandId = brandId
        )

    protected fun createCategory(id: Int) =
        CategoryEntity(
            id = id,
            name = "Category $id",
            imagePath = "category_${id}_image_path"
        )

    protected fun createColor(id: Int) =
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

    protected fun createProductImage(id: Int, productItemId: Int) =
        ProductImageEntity(
            id = id,
            imagePath = "product_image_${id}_path",
            productItemId = productItemId
        )

    protected fun createProductItem(id: Int, productId: Int, colorId: Int) =
        ProductItemEntity(
            id = id,
            productId = productId,
            colorId = colorId
        )

    protected fun createUser(id: Int) =
        UserEntity(
            id = id,
            name = "Jane",
            surname = "Doe",
            phone = "9100000000",
            avatar = null
        )
}
