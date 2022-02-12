package ru.babaetskv.passionwoman.domain.exceptions

abstract class EmptyDataException(
    override val message: String?
) : Exception()
