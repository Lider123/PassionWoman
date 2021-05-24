package ru.babaetskv.passionwoman.data.api

interface ApiProvider {
    fun provideCommonApi(): CommonApi
    fun provideAuthApi(): PassionWomanApi
}
