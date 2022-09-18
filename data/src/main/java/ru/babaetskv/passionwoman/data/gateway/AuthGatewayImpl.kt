package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.gateway.base.BaseGatewayImpl
import ru.babaetskv.passionwoman.data.model.AccessTokenModel
import ru.babaetskv.passionwoman.domain.AppDispatchers
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway

class AuthGatewayImpl(
    private val api: CommonApi,
    stringProvider: StringProvider,
    dispatchers: AppDispatchers
) : BaseGatewayImpl(stringProvider, dispatchers), AuthGateway {

    override suspend fun authorize(accessToken: String): String = networkRequest {
        api.authorize(AccessTokenModel(accessToken)).token
    }
}
