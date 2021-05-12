package ru.babaetskv.passionwoman.app.presentation.base

import androidx.recyclerview.widget.DiffUtil

class EqualDiffUtilCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSame(oldItem, newItem)

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
