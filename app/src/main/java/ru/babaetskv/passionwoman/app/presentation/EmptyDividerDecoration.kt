package ru.babaetskv.passionwoman.app.presentation

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class EmptyDividerDecoration(
    context: Context,
    @DimenRes cardInsets: Int,
    private val applyOutsideDecoration: Boolean = true
) : RecyclerView.ItemDecoration() {
    private val spacing: Int = context.resources.getDimensionPixelSize(cardInsets)
    private var displayMode: Int = -1

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        val layoutManager = parent.layoutManager
        setSpacingForDirection(outRect, layoutManager, position, itemCount)
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        layoutManager: RecyclerView.LayoutManager?,
        position: Int,
        itemCount: Int
    ) {
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager)
        }

        when (displayMode) {
            HORIZONTAL -> {
                outRect.left = if (applyOutsideDecoration || (position > 0)) spacing else 0
                outRect.right =
                    if (applyOutsideDecoration && (position == itemCount - 1)) spacing else 0
                outRect.top = 0
                outRect.bottom = 0
            }
            VERTICAL -> {
                outRect.left = 0
                outRect.right = 0
                outRect.top = if (applyOutsideDecoration || (position > 0)) spacing else 0
                outRect.bottom =
                    if (applyOutsideDecoration && (position == itemCount - 1)) spacing else 0
            }
            GRID -> {
                outRect.left = spacing
                outRect.right = spacing
                outRect.top = spacing
                outRect.bottom = spacing
            }
        }
    }

    private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager?): Int {
        return when (layoutManager) {
            is StaggeredGridLayoutManager -> GRID
            is GridLayoutManager -> GRID
            else -> if (layoutManager!!.canScrollHorizontally()) HORIZONTAL else VERTICAL
        }
    }

    companion object {
        private const val HORIZONTAL = 0
        private const val VERTICAL = 1
        private const val GRID = 2
    }
}