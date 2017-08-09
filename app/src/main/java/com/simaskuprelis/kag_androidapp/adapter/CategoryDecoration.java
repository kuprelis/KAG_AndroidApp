package com.simaskuprelis.kag_androidapp.adapter;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.simaskuprelis.kag_androidapp.entity.NodeListItem;

public class CategoryDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mHeight;

    public CategoryDecoration(Context c) {
        mPaint = new Paint();
        mPaint.setARGB(31, 0, 0, 0);
        mPaint.setStyle(Paint.Style.FILL);
        mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, c.getResources().getDisplayMetrics());
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (hasDivider(view, parent, state)) {
                c.drawRect(view.getLeft(), view.getTop() - mHeight, view.getRight(), view.getTop(), mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (hasDivider(view, parent, state)) {
            outRect.set(0, mHeight, 0, 0);
        } else {
            outRect.setEmpty();
        }
    }

    private boolean hasDivider(View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int viewType = parent.getAdapter().getItemViewType(position);
        return position != 0 && position < state.getItemCount()
                && viewType == NodeListItem.TYPE_CATEGORY;
    }
}
