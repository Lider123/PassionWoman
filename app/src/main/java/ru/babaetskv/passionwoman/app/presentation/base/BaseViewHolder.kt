package ru.babaetskv.passionwoman.app.presentation.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : Any>(v : View) : RecyclerView.ViewHolder(v) {
    protected val context: Context = v.context

    abstract fun bind(item: T)

    open fun bind(item: T, payload: Any){
        bind(item)
    }
}
