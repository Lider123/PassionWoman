package ru.babaetskv.passionwoman.app.presentation.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : Any>(v : View) : RecyclerView.ViewHolder(v) {

    abstract fun bind(item: T)

    open fun bind(item: T, payload: Any){
        bind(item)
    }
}
