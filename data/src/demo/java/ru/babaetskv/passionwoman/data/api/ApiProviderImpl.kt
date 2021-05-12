package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager

class ApiProviderImpl(
    private val assetManager: AssetManager
) : ApiProvider {

    override fun provideApi(): Api = ApiImpl(assetManager)
}
