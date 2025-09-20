package ru.babaetskv.passionwoman.data.gateway.provider

import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.gateway.*
import ru.babaetskv.passionwoman.data.gateway.decorator.*
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.domain.gateway.*

class GatewayProviderImpl(
    private val commonApi: CommonApi,
    private val authApi: AuthApi,
    private val exceptionProvider: GatewayExceptionProvider
) : GatewayProvider {

    override fun provideAuthGateway(): AuthGateway =
        AuthGatewayImpl(commonApi)
            .let { SafeAuthGatewayDecorator(it, exceptionProvider) }

    override fun provideCartGateway(): CartGateway =
        CartGatewayImpl(authApi)
            .let { SafeCartGatewayDecorator(it, exceptionProvider) }

    override fun provideCatalogGateway(): CatalogGateway =
        CatalogGatewayImpl(commonApi)
            .let { SafeCatalogGatewayDecorator(it, exceptionProvider) }

    override fun provideProfileGateway(): ProfileGateway =
        ProfileGatewayImpl(authApi)
            .let { SafeProfileGatewayDecorator(it, exceptionProvider) }

    override fun providePushGateway(): PushGateway =
        PushGatewayImpl(authApi)
            .let { SafePushGatewayDecorator(it, exceptionProvider) }
}
