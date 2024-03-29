package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.data.assets.AssetProvider
import ru.babaetskv.passionwoman.data.model.ProductModel
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.data.repository.ProductsRepository
import ru.babaetskv.passionwoman.domain.usecase.InitDatabaseUseCase

class InitDatabaseInteractor(
    private val repository: ProductsRepository,
    private val assetProvider: AssetProvider,
    private val appPrefs: AppPreferences,
    private val stringProvider: StringProvider,
) : BaseInteractor<Unit, Unit>(), InitDatabaseUseCase {

    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        InitDatabaseUseCase.InitDatabaseException(cause, stringProvider)

    override suspend fun run(params: Unit) {
        val products: List<ProductModel> =
            assetProvider.loadListFromAsset(
                AssetProvider.AssetFile.PRODUCTS,
                ProductModel::class.java
            )
        for (product in products) repository.saveProduct(product.transform())
        repository.dump()
        appPrefs.databaseFilled = true
    }
}
