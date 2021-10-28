package ru.babaetskv.passionwoman.domain.interactor.exception

abstract class EmptyDataException(
    override val message: String?
) : Exception()
