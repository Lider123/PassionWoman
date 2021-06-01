package ru.babaetskv.passionwoman.app.presentation.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<T : Any>(callback: DiffUtil.ItemCallback<T>) : ListAdapter<T, BaseViewHolder<T>>(callback) {

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        for (payload in payloads) {
            holder.bind(getItem(position), payload)
        }
    }
}
