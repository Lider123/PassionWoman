package ru.babaetskv.passionwoman.data.api

import android.content.Context

class ApiProviderImpl(
    private val context: Context
) : ApiProvider {

    override fun provideApi(): Api = ApiImpl(context.assets)
}
