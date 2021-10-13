package ru.babaetskv.passionwoman.domain.interactor.exception

abstract class NetworkDataException(
    override val message: String?,
    cause: Exception?,
    val dataIsOptional: Boolean = false
) : Exception(cause)
