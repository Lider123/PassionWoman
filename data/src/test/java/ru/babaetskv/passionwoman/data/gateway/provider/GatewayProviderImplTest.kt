package ru.babaetskv.passionwoman.data.gateway.provider

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.gateway.decorator.SafeAuthGatewayDecorator
import ru.babaetskv.passionwoman.data.gateway.decorator.SafeCartGatewayDecorator
import ru.babaetskv.passionwoman.data.gateway.decorator.SafeCatalogGatewayDecorator
import ru.babaetskv.passionwoman.data.gateway.decorator.SafeProfileGatewayDecorator
import ru.babaetskv.passionwoman.data.gateway.decorator.base.AuthGatewayDecorator
import ru.babaetskv.passionwoman.data.gateway.decorator.base.CartGatewayDecorator
import ru.babaetskv.passionwoman.data.gateway.decorator.base.CatalogGatewayDecorator
import ru.babaetskv.passionwoman.data.gateway.decorator.base.ProfileGatewayDecorator
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider

@RunWith(MockitoJUnitRunner::class)
class GatewayProviderImplTest {
    @Mock
    lateinit var commonApiMock: CommonApi
    @Mock
    lateinit var authApiMock: AuthApi
    @Mock
    lateinit var exceptionProviderMock: GatewayExceptionProvider
    @InjectMocks
    lateinit var provider: GatewayProviderImpl

    @Test
    fun provideAuthGateway_returnsAuthGatewayWithDecorator() {
        val result = provider.provideAuthGateway()

        assertTrue(result is SafeAuthGatewayDecorator)
        result as AuthGatewayDecorator
        assertTrue(result.gateway !is AuthGatewayDecorator)
    }

    @Test
    fun provideCartGateway_returnsCartGatewayWithDecorator() {
        val result = provider.provideCartGateway()

        assertTrue(result is SafeCartGatewayDecorator)
        result as CartGatewayDecorator
        assertTrue(result.gateway !is CartGatewayDecorator)
    }

    @Test
    fun provideCatalogGateway_returnsCatalogGatewayWithDecorator() {
        val result = provider.provideCatalogGateway()

        assertTrue(result is SafeCatalogGatewayDecorator)
        result as CatalogGatewayDecorator
        assertTrue(result.gateway !is CatalogGatewayDecorator)
    }

    @Test
    fun provideProfileGateway_returnsProfileGatewayWithDecorator() {
        val result = provider.provideProfileGateway()

        assertTrue(result is SafeProfileGatewayDecorator)
        result as ProfileGatewayDecorator
        assertTrue(result.gateway !is ProfileGatewayDecorator)
    }
}
