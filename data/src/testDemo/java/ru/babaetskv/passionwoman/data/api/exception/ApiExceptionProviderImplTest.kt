package ru.babaetskv.passionwoman.data.api.exception

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ApiExceptionProviderImplTest {
    @InjectMocks
    lateinit var provider: ApiExceptionProviderImpl

    @Test
    fun getUnauthorizedException_returnsHttpExceptionWithCode401() {
        val result = provider.getUnauthorizedException("message")

        assertEquals(401, result.code())
        assertEquals("message", result.response()?.errorBody()?.string())
    }

    @Test
    fun getNotFoundException_returnsHttpExceptionWithCode404() {
        val result = provider.getNotFoundException("message")

        assertEquals(404, result.code())
        assertEquals("message", result.response()?.errorBody()?.string())
    }

    @Test
    fun getBadRequestException_returnsHttpExceptionWithCode400() {
        val result = provider.getBadRequestException("message")

        assertEquals(400, result.code())
        assertEquals("message", result.response()?.errorBody()?.string())
    }

    @Test
    fun getInternalServerErrorException_returnsHttpExceptionWithCode500() {
        val result = provider.getInternalServerErrorException("message")

        assertEquals(500, result.code())
        assertEquals("message", result.response()?.errorBody()?.string())
    }
}