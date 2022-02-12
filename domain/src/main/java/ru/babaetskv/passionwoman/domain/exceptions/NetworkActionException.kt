package ru.babaetskv.passionwoman.domain.exceptions

abstract class NetworkActionException(
    override val message: String,
    cause: Exception?
) : Exception(cause)
