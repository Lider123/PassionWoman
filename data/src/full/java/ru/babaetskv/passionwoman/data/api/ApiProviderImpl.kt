package ru.babaetskv.passionwoman.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.babaetskv.passionwoman.data.BuildConfig
import ru.babaetskv.passionwoman.data.preferences.Preferences
import java.util.concurrent.TimeUnit

class ApiProviderImpl(
    context: Context,
    private val moshi: Moshi,
    private val preferences: Preferences
) : ApiProvider {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else HttpLoggingInterceptor.Level.NONE
    }
    private val httpClientBuilder: OkHttpClient.Builder
        get() = OkHttpClient.Builder()
            .readTimeout(15L, TimeUnit.SECONDS)
            .writeTimeout(15L, TimeUnit.SECONDS)
            .connectTimeout(15L, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
            }
    private val commonHttpClient: OkHttpClient
        get() = httpClientBuilder
            .build()
    private val authHttpClient: OkHttpClient
        get() = httpClientBuilder
            .addInterceptor(AuthInterceptor(preferences))
            .build()

    override fun provideAuthApi(): PassionWomanApi =
        createRetrofit(authHttpClient)
            .create(PassionWomanApi::class.java)

    override fun provideCommonApi(): CommonApi =
        createRetrofit(commonHttpClient)
            .create(CommonApi::class.java)

    private fun createRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
}
