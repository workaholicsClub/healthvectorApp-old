package ru.android.childdiary.presentation.core.adapters.decorators;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable divider;
    private final int padding;

    public DividerItemDecoration(Drawable divider, int padding) {
        this.divider = divider;
        this.padding = padding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);

        int itemsCount = state.getItemCount();
        if (itemsCount > 1 && parent.getChildAdapterPosition(view) != 0) {
            outRect.top = divider.getIntrinsicHeight();
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft() + padding;
        int dividerRight = parent.getWidth() - parent.getPaddingRight() - padding;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; ++i) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + divider.getIntrinsicHeight();

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(canvas);
        }
    }
}
