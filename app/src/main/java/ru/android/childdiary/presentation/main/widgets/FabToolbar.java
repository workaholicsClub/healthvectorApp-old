package ru.android.childdiary.presentation.main.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;
import ru.android.childdiary.R;

public class FabToolbar extends RevealFrameLayout {
    private static final int ANIMATION_DURATION = 500;
    private final OnFabClickListener onFabClickListener = new OnFabClickListener();
    private final AnimatorListener animatorListener = new AnimatorListener();

    @BindView(R.id.bottomPanel)
    View bottomPanel;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private float screenWidth;

    public FabToolbar(Context context) {
        super(context);
        init();
    }

    public FabToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FabToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        inflate(getContext(), R.layout.fab_toolbar, this);
        ButterKnife.bind(this);
        fab.setOnClickListener(onFabClickListener);
    }

    public void setColor(@ColorInt int color) {
        bottomPanel.setBackgroundColor(color);
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void show() {
        fab.setOnClickListener(null);
        fab.hide();
        bottomPanel.setVisibility(VISIBLE);
        animateCircle(0, screenWidth, null);
    }

    public boolean hide() {
        boolean isVisible = bottomPanel.getVisibility() == VISIBLE;
        if (isVisible) {
            animateCircle(screenWidth, 0, animatorListener);
            return true;
        }
        return false;
    }

    private void animateCircle(float startRadius, float endRadius, SupportAnimator.AnimatorListener listener) {
        int cx = (fab.getLeft() + fab.getRight()) / 2;
        int cy = (fab.getTop() + fab.getBottom()) / 2;

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(bottomPanel, cx, cy, startRadius, endRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(ANIMATION_DURATION);
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.start();
    }

    private class OnFabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            show();
        }
    }

    private class AnimatorListener implements SupportAnimator.AnimatorListener {
        @Override
        public void onAnimationStart() {
        }

        @Override
        public void onAnimationEnd() {
            bottomPanel.setVisibility(GONE);
            fab.show();
            fab.setOnClickListener(onFabClickListener);
        }

        @Override
        public void onAnimationCancel() {
        }

        @Override
        public void onAnimationRepeat() {
        }
    }
}
