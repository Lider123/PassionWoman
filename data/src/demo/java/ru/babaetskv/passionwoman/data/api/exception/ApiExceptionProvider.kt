package ru.babaetskv.passionwoman.data.api.exception

import retrofit2.HttpException

interface ApiExceptionProvider {
    fun getUnauthorizedException(message: String) : HttpException
    fun getNotFoundException(message: String) : HttpException
    fun getBadRequestException(message: String) : HttpException
    fun getInternalServerErrorException(message: String?) : HttpException
}
