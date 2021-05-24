package ru.babaetskv.passionwoman.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import ru.babaetskv.passionwoman.data.preferences.Preferences

class ApiProviderImpl(
    private val context: Context,
    private val moshi: Moshi,
    private val preferences: Preferences
) : ApiProvider {

    override fun provideAuthApi(): PassionWomanApi = PassionWomanApiImpl(moshi, context.assets)

    override fun provideCommonApi(): CommonApi = CommonApiImpl()
}
