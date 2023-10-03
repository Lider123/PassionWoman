package ru.babaetskv.passionwoman.data.database.entity.transformations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.CartItemEntity
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform

class CartItemTransformableParamsProvider(
    private val database: PassionWomanDatabase
) : CartItemEntity.TransformableParamsProvider {

    override suspend fun provideColor(colorId: Long): ColorModel = withContext(Dispatchers.IO) {
        return@withContext database.colorDao.getById(colorId)
            ?.transform()
            ?: throw IllegalStateException("Failed to found color with id $colorId")
    }
}
