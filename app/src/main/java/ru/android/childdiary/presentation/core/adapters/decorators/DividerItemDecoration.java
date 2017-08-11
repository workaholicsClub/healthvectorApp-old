package ru.android.childdiary.presentation.core.adapters.decorators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.android.childdiary.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable divider;
    private final int dividerPadding, extraPadding;
    private final boolean paintDividers;

    public DividerItemDecoration(Context context, boolean paintDividers) {
        this.divider = ContextCompat.getDrawable(context, R.drawable.divider);
        this.dividerPadding = context.getResources().getDimensionPixelSize(R.dimen.divider_padding);
        this.extraPadding = context.getResources().getDimensionPixelSize(R.dimen.base_margin);
        this.paintDividers = paintDividers;
    }

    public DividerItemDecoration(Context context) {
        this(context, true);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);

        int itemsCount = state.getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if (paintDividers && itemsCount > 1 && position > 0) {
            outRect.top = divider.getIntrinsicHeight();
        }
        if (position == 0) {
            outRect.top += extraPadding;
        }
        if (position == itemsCount - 1) {
            outRect.bottom += extraPadding;
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (!paintDividers) {
            return;
        }

        int dividerLeft = parent.getPaddingLeft() + dividerPadding;
        int dividerRight = parent.getWidth() - parent.getPaddingRight() - dividerPadding;

        int childCount = parent.getChildCount();
        for (int i = 1; i < childCount; ++i) {
            View child = parent.getChildAt(i);

            int dividerBottom = child.getTop();
            int dividerTop = dividerBottom - divider.getIntrinsicHeight();

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(canvas);
        }
    }
}
