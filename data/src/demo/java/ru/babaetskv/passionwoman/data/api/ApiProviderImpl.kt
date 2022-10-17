package ru.babaetskv.passionwoman.data.api

import android.content.Context
import ru.babaetskv.passionwoman.data.AssetProvider
import ru.babaetskv.passionwoman.data.database.DatabaseProvider
import ru.babaetskv.passionwoman.data.database.entity.transformations.ProductItemTransformableParamsProvider
import ru.babaetskv.passionwoman.data.database.entity.transformations.ProductTransformableParamsProvider
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class ApiProviderImpl(
    context: Context,
    private val assetProvider: AssetProvider,
    private val authPreferences: AuthPreferences,
    private val dateTimeConverter: DateTimeConverter
) : ApiProvider {
    private val database = DatabaseProvider.provideDatabase(context)
    private val productTransformableParamsProvider =
        ProductTransformableParamsProvider(
            database,
            ProductItemTransformableParamsProvider(database)
        )

    override fun provideAuthApi(): AuthApi =
        AuthApiImpl(
            database,
            authPreferences,
            dateTimeConverter
        )

    override fun provideCommonApi(): CommonApi =
        CommonApiImpl(
            database,
            assetProvider,
            productTransformableParamsProvider,
        )
}
