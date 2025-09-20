package ru.babaetskv.passionwoman.data.filters

class BooleanBuilder(
    private var value: Boolean
) {

    fun and(other: Boolean): BooleanBuilder = apply {
        value = value && other
    }

    fun andNot(other: Boolean): BooleanBuilder = apply {
        value = value && !other
    }

    fun build(): Boolean = value
}
