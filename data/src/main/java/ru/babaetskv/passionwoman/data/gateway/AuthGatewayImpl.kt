package ru.babaetskv.passionwoman.data.gateway

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.gateway.base.BaseGatewayImpl
import ru.babaetskv.passionwoman.data.model.AccessTokenModel
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway

class AuthGatewayImpl(
    private val api: CommonApi,
    stringProvider: StringProvider
) : BaseGatewayImpl(stringProvider), AuthGateway {

    override suspend fun authorize(accessToken: String): String = withContext(Dispatchers.IO) {
        return@withContext networkRequest {
            api.authorize(AccessTokenModel(accessToken)).token
        }
    }
}
