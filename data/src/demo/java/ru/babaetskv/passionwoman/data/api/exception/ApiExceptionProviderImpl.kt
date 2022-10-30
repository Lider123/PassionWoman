package ru.babaetskv.passionwoman.data.api.exception

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class ApiExceptionProviderImpl : ApiExceptionProvider {

    override fun getUnauthorizedException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<ResponseBody>(UNAUTHORIZED, it)
        }.let(::HttpException)

    override fun getNotFoundException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<ResponseBody>(NOT_FOUND, it)
        }.let(::HttpException)

    override fun getBadRequestException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<ResponseBody>(BAD_REQUEST, it)
        }.let(::HttpException)

    override fun getInternalServerErrorException(message: String?) : HttpException =
        message.let {
            it ?: "Internal server error"
        }.toResponseBody("text/plain".toMediaType()).let {
            Response.error<ResponseBody>(INTERNAL_SERVER_ERROR, it)
        }.let(::HttpException)

    companion object {
        @JvmStatic
        private val UNAUTHORIZED = 401
        @JvmStatic
        private val NOT_FOUND = 404
        @JvmStatic
        private val BAD_REQUEST = 400
        @JvmStatic
        private val INTERNAL_SERVER_ERROR = 500
    }
}
