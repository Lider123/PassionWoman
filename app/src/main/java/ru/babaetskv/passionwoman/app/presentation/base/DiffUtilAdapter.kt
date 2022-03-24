package ru.babaetskv.passionwoman.app.presentation.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class DiffUtilAdapter<T : Any> : RecyclerView.Adapter<BaseViewHolder<T>>() {
    private val items = mutableListOf<T>()

    abstract fun createDiffUtilCallback(oldList: List<T>, newList: List<T>): Callback<T>

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        for (payload in payloads) {
            holder.bind(items[position], payload)
        }
    }

    fun submitData(newData: List<T>) {
        val callback = createDiffUtilCallback(items, newData)
        val diff = DiffUtil.calculateDiff(callback)
        diff.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newData)
    }

    abstract class Callback<T>(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {

        abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean
        abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return areContentsTheSame(oldItem, newItem)
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return getChangePayload(oldItem, newItem)
        }

        open fun getChangePayload(oldItem: T, newItem: T): Any? = null
    }
}
