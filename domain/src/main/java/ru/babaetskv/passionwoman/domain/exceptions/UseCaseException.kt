package ru.babaetskv.passionwoman.domain.exceptions

sealed class UseCaseException(
    override val cause: Exception?,
    override val message: String,
) : Exception() {
    open val isCritical: Boolean = true

    abstract class Data(
        cause: Exception,
        message: String,
    ) : UseCaseException(cause, message)

    abstract class EmptyData(
        message: String
    ) : UseCaseException(null, message) {
        override val isCritical: Boolean = false
    }

    abstract class Action(
        cause: Exception,
        message: String
    ) : UseCaseException(cause, message)
}
