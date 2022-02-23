package ru.babaetskv.passionwoman.domain.model.base

abstract class PagedResponse<T> : List<T> {
    abstract val items: List<T>
    abstract val total: Int

    override val size: Int
        get() = items.size

    override fun contains(element: T): Boolean = items.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = items.containsAll(elements)

    override fun get(index: Int): T = items[index]

    override fun indexOf(element: T): Int = items.indexOf(element)

    override fun isEmpty(): Boolean = items.isEmpty()

    override fun iterator(): Iterator<T> = items.iterator()

    override fun lastIndexOf(element: T): Int = items.lastIndexOf(element)

    override fun listIterator(): ListIterator<T> = items.listIterator()

    override fun listIterator(index: Int): ListIterator<T> = items.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<T> = items.subList(fromIndex, toIndex)
}
