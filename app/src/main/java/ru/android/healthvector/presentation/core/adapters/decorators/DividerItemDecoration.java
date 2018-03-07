package ru.android.healthvector.presentation.core.adapters.decorators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable divider;
    private final int dividerPadding, extraPadding;
    private final boolean paintDividers;
    private final boolean useFooter;

    public DividerItemDecoration(Context context, BaseRecyclerViewAdapter adapter) {
        this(context, adapter.paintDividers(), adapter.useFooter());
    }

    public DividerItemDecoration(Context context, boolean paintDividers, boolean useFooter) {
        this.divider = ContextCompat.getDrawable(context, R.drawable.divider);
        this.dividerPadding = context.getResources().getDimensionPixelSize(R.dimen.divider_padding);
        this.extraPadding = context.getResources().getDimensionPixelSize(R.dimen.base_margin);
        this.paintDividers = paintDividers;
        this.useFooter = useFooter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        outRect.set(0, 0, 0, 0);

        int count = state.getItemCount();
        int last = useFooter ? count - 2 : count - 1;

        if (paintDividers && position >= 1 && position <= last) {
            outRect.top = divider.getIntrinsicHeight();
        }
        if (position == 0) {
            outRect.top += extraPadding;
        }
        if (position == last) {
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

        int count = state.getItemCount();
        int last = useFooter ? count - 2 : count - 1;

        for (int i = 0; i < parent.getChildCount(); ++i) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position >= 1 && position <= last) {
                int dividerBottom = child.getTop();
                int dividerTop = dividerBottom - divider.getIntrinsicHeight();

                divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                divider.draw(canvas);
            }
        }
    }
}
