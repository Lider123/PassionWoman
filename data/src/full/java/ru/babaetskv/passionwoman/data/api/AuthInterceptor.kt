package ru.babaetskv.passionwoman.data.api

import okhttp3.Interceptor
import okhttp3.Response
import ru.babaetskv.passionwoman.data.preferences.Preferences

class AuthInterceptor(
    private val preferences: Preferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        requestBuilder.removeHeader(AUTHORIZATION)
        if (preferences.authType == Preferences.AuthType.AUTHORIZED) {
            val authHeader = String.format(TOKEN_TEMPLATE, preferences.authToken)
            requestBuilder.addHeader(AUTHORIZATION, authHeader)
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val TOKEN_TEMPLATE = "Token %s"
    }
}
