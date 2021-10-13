package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BasePagingAdapter<T : Any>(
    callback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, BaseViewHolder<T>>(callback) {

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        for (payload in payloads) getItem(position)?.let { holder.bind(it, payload) }
    }

    fun submitList(lifecycle: Lifecycle, data: List<T>) {
        submitData(lifecycle, PagingData.from(data))
    }
}
