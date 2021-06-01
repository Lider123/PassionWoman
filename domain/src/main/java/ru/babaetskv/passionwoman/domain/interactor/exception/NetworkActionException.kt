package ru.babaetskv.passionwoman.domain.interactor.exception

abstract class NetworkActionException(
    override val message: String,
    cause: Exception?
) : Exception(cause)
