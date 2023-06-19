package ru.babaetskv.passionwoman.data.api

import android.content.Context
import ru.babaetskv.passionwoman.data.assets.AssetProvider
import ru.babaetskv.passionwoman.data.api.decorator.CheckTokenAuthApiDecorator
import ru.babaetskv.passionwoman.data.api.decorator.DelayAuthApiDecorator
import ru.babaetskv.passionwoman.data.api.decorator.DelayCommonApiDecorator
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProviderImpl
import ru.babaetskv.passionwoman.data.database.DatabaseProvider
import ru.babaetskv.passionwoman.data.database.entity.transformations.CartItemTransformableParamsProvider
import ru.babaetskv.passionwoman.data.database.entity.transformations.OrderTransformableParamsProvider
import ru.babaetskv.passionwoman.data.database.entity.transformations.ProductItemTransformableParamsProvider
import ru.babaetskv.passionwoman.data.database.entity.transformations.ProductTransformableParamsProvider
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class ApiProviderImpl(
    context: Context,
    private val assetProvider: AssetProvider,
    private val authPrefs: AuthPreferences,
    appPrefs: AppPreferences,
    private val dateTimeConverter: DateTimeConverter
) : ApiProvider {
    private val database = DatabaseProvider.provideDatabase(context, appPrefs)
    private val exceptionProvider: ApiExceptionProvider = ApiExceptionProviderImpl()
    private val productTransformableParamsProvider =
        ProductTransformableParamsProvider(
            database,
            exceptionProvider,
            ProductItemTransformableParamsProvider(database)
        )
    private val orderTransformableParamsProvider =
        OrderTransformableParamsProvider(
            database,
            exceptionProvider,
            CartItemTransformableParamsProvider(database)
        )

    override fun provideAuthApi(): AuthApi =
        AuthApiImpl(
            database,
            exceptionProvider,
            authPrefs,
            dateTimeConverter,
            orderTransformableParamsProvider
        )
            .let { CheckTokenAuthApiDecorator(authPrefs, exceptionProvider, it) }
            .let(::DelayAuthApiDecorator)

    override fun provideCommonApi(): CommonApi =
        CommonApiImpl(
            database,
            assetProvider,
            exceptionProvider,
            productTransformableParamsProvider
        )
            .let(::DelayCommonApiDecorator)
}
