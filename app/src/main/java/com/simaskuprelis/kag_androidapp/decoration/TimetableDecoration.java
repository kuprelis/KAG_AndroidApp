package com.simaskuprelis.kag_androidapp.decoration;


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

    private Paint paint;
    private int height;

    public TimetableDecoration(Context c) {
        paint = new Paint();
        paint.setARGB(31, 0, 0, 0);
        paint.setStyle(Paint.Style.FILL);
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, c.getResources().getDisplayMetrics());
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 1; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            TextView tv = view.findViewById(R.id.lesson_name);
            c.drawRect(tv.getLeft(), view.getTop(), view.getRight(), view.getTop() - height, paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) > 0) {
            outRect.set(0, height, 0, 0);
        } else {
            outRect.setEmpty();
        }
    }
}
