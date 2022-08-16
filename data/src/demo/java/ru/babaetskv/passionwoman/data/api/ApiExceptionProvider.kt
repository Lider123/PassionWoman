package ru.babaetskv.passionwoman.data.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

object ApiExceptionProvider {
    @JvmStatic
    private val UNAUTHORIZED = 401
    @JvmStatic
    private val NOT_FOUND = 404
    @JvmStatic
    private val BAD_REQUEST = 400
    @JvmStatic
    private val INTERNAL_SERVER_ERROR = 500

    fun getUnauthorizedException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<ResponseBody>(UNAUTHORIZED, it)
        }.let(::HttpException)

    fun getNotFoundException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<ResponseBody>(NOT_FOUND, it)
        }.let(::HttpException)

    fun getBadRequestException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<ResponseBody>(BAD_REQUEST, it)
        }.let(::HttpException)

    fun getInternalServerErrorException(message: String?) : HttpException =
        message.let {
            it ?: "Internal server error"
        }.toResponseBody("text/plain".toMediaType()).let {
            Response.error<ResponseBody>(INTERNAL_SERVER_ERROR, it)
        }.let(::HttpException)
}
