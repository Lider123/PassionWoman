package ru.babaetskv.passionwoman.app.utils.view;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LinearLayoutPagerManager extends LinearLayoutManager {
    private final int itemsPerPage;
    private final int edgeItemMargin;

    public LinearLayoutPagerManager(
            Context context,
            int orientation,
            boolean reverseLayout,
            int itemsPerPage,
            int edgeItemMargin
    ) {
        super(context, orientation, reverseLayout);
        this.itemsPerPage = itemsPerPage;
        this.edgeItemMargin = edgeItemMargin;
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return super.checkLayoutParams(lp) && lp.width == getItemSize();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return setProperItemSize(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return setProperItemSize(super.generateLayoutParams(lp));
    }

    private RecyclerView.LayoutParams setProperItemSize(RecyclerView.LayoutParams lp) {
        int itemSize = getItemSize();
        if (getOrientation() == HORIZONTAL) {
            lp.width = itemSize;
        } else {
            lp.height = itemSize;
        }
        return lp;
    }

    private int getItemSize() {
        int pageSize;
        if (getOrientation() == HORIZONTAL) {
            int paddingHorizontal = getPaddingStart() + getPaddingEnd();
            pageSize = getWidth() - paddingHorizontal - edgeItemMargin;
        } else {
            int paddingVertical = getPaddingTop() + getPaddingBottom();
            pageSize = getHeight() - paddingVertical - edgeItemMargin;
        }
        return Math.round((float) pageSize / itemsPerPage);
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }
}
