package com.simaskuprelis.kag_androidapp.adapter;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.simaskuprelis.kag_androidapp.R;

public class TimetableDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mHeight;

    public TimetableDecoration(Context c) {
        mPaint = new Paint();
        mPaint.setARGB(31, 0, 0, 0);
        mPaint.setStyle(Paint.Style.FILL);
        mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, c.getResources().getDisplayMetrics());
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View view = parent.getChildAt(i);
            TextView tv = view.findViewById(R.id.lesson_name);
            c.drawRect(tv.getLeft(), view.getBottom(), view.getRight(), view.getBottom() + mHeight, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) < parent.getChildCount() - 1) {
            outRect.set(0, 0, 0, mHeight);
        } else {
            outRect.setEmpty();
        }
    }
}
