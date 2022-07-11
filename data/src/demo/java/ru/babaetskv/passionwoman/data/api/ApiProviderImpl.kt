package ru.babaetskv.passionwoman.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import ru.babaetskv.passionwoman.domain.DateTimeConverter

class ApiProviderImpl(
    private val context: Context,
    private val moshi: Moshi,
    private val dateTimeConverter: DateTimeConverter
) : ApiProvider {

    override fun provideAuthApi(): AuthApi = AuthApiImpl(moshi, context.assets, dateTimeConverter)

    override fun provideCommonApi(): CommonApi = CommonApiImpl(moshi, context.assets)
}
