package ru.babaetskv.passionwoman.data.gateway.provider

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway

interface GatewayProvider {
    fun provideAuthGateway(): AuthGateway
    fun provideCartGateway(): CartGateway
    fun provideCatalogGateway(): CatalogGateway
    fun provideProfileGateway(): ProfileGateway
}
