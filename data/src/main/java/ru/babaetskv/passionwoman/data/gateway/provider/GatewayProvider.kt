package ru.babaetskv.passionwoman.data.gateway.provider

import ru.babaetskv.passionwoman.domain.gateway.*

interface GatewayProvider {
    fun provideAuthGateway(): AuthGateway
    fun provideCartGateway(): CartGateway
    fun provideCatalogGateway(): CatalogGateway
    fun provideProfileGateway(): ProfileGateway
    fun providePushGateway(): PushGateway
}
