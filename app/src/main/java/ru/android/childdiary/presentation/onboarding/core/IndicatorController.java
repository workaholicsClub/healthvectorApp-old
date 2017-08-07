package ru.android.childdiary.presentation.onboarding.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ru.android.childdiary.R;

class IndicatorController {
    int mCurrentPosition;
    private Context mContext;
    private LinearLayout mDotLayout;
    private List<ImageView> mDots;
    private int mSlideCount;

    public void initialize(LinearLayout view, int slideCount) {
        mDotLayout = view;
        mContext = view.getContext();
        mDots = new ArrayList<>();
        mSlideCount = slideCount;
        for (int i = 0; i < slideCount; i++) {
            ImageView dot = new ImageView(mContext);
            dot.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.indicator_dot_grey));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            mDotLayout.addView(dot, params);
            mDots.add(dot);
        }
        selectPosition(0);
    }

    public void selectPosition(int index) {
        mCurrentPosition = index;
        for (int i = 0; i < mSlideCount; i++) {
            int drawableId = (i == index) ?
                    (R.drawable.indicator_dot_white) : (R.drawable.indicator_dot_grey);
            Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
            mDots.get(i).setImageDrawable(drawable);
        }
    }
}
