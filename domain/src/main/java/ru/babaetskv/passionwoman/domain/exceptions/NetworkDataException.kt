package ru.babaetskv.passionwoman.domain.exceptions

abstract class NetworkDataException(
    override val message: String?,
    cause: Exception?,
    val dataIsOptional: Boolean = false
) : Exception(cause)
