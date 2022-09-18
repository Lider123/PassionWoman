package ru.babaetskv.passionwoman.data.api

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.transformations.ProductItemTransformableParamsProvider
import ru.babaetskv.passionwoman.data.database.entity.transformations.ProductTransformableParamsProvider
import ru.babaetskv.passionwoman.domain.AppDispatchers
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class ApiProviderImpl(
    private val context: Context,
    private val moshi: Moshi,
    private val authPreferences: AuthPreferences,
    private val dateTimeConverter: DateTimeConverter,
    private val dispatchers: AppDispatchers
) : ApiProvider {
    private val database = Room.databaseBuilder(context, PassionWomanDatabase::class.java, "passionwoman")
        .createFromAsset(DATABASE_FILENAME)
        .build()
    private val productTransformableParamsProvider =
        ProductTransformableParamsProvider(
            database,
            ProductItemTransformableParamsProvider(database),
            dispatchers
        )

    override fun provideAuthApi(): AuthApi =
        AuthApiImpl(
            context.assets,
            database,
            moshi,
            authPreferences,
            dateTimeConverter,
            dispatchers
        )

    override fun provideCommonApi(): CommonApi =
        CommonApiImpl(
            context.assets,
            database,
            productTransformableParamsProvider,
            moshi,
            dispatchers
        )

    companion object {
        private const val DATABASE_FILENAME = "demo_db_editor/passionwoman.db"
    }
}
