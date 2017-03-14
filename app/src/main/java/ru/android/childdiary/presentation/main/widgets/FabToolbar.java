package ru.android.childdiary.presentation.main.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
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

    private int cx, cy;
    private int screenWidth;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int fabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
            int fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);
            int distanceFromScreenEdge = fabSize / 2 + fabMargin;
            int toolbarHeight = getResources().getDimensionPixelSize(R.dimen.fab_toolbar_height);
            cx = screenWidth - distanceFromScreenEdge;
            cx = cx < 0 ? 0 : cx;
            cy = toolbarHeight - distanceFromScreenEdge;
            cy = cy < 0 ? 0 : cy;
        }

        inflate(getContext(), R.layout.fab_toolbar, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        fab.setOnClickListener(onFabClickListener);
    }

    public void setColor(@ColorInt int color) {
        bottomPanel.setBackgroundColor(color);
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void show() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cx = (fab.getLeft() + fab.getRight()) / 2;
            cy = (fab.getTop() + fab.getBottom()) / 2;
        }

        fab.setOnClickListener(null);
        fab.hide();
        bottomPanel.setVisibility(VISIBLE);
        animateCircle(0, screenWidth, null);
    }

    public void showButton() {
        if (fab.getVisibility() == GONE) {
            fab.show();
        }
    }

    public boolean hide() {
        boolean isVisible = bottomPanel.getVisibility() == VISIBLE;
        if (isVisible) {
            animateCircle(screenWidth, 0, animatorListener);
            return true;
        }
        return false;
    }

    public void hideButton() {
        if (fab.getVisibility() == VISIBLE) {
            fab.hide();
        }
    }

    private void animateCircle(float startRadius, float endRadius, SupportAnimator.AnimatorListener listener) {
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
