package ru.babaetskv.passionwoman.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class ApiProviderImpl(
    private val context: Context,
    private val moshi: Moshi,
    private val authPreferences: AuthPreferences,
    private val dateTimeConverter: DateTimeConverter
) : ApiProvider {

    override fun provideAuthApi(): AuthApi =
        AuthApiImpl(moshi, context.assets, authPreferences, dateTimeConverter)

    override fun provideCommonApi(): CommonApi = CommonApiImpl(moshi, context.assets)
}
