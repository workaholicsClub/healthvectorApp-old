package ru.android.healthvector.presentation.core.widgets;

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
import ru.android.healthvector.R;

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
        inflate(getContext(), R.layout.fab_toolbar, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        fab.setOnClickListener(onFabClickListener);
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
    }

    public void setColor(@ColorInt int color) {
        bottomPanel.setBackgroundColor(color);
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void showBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cx = (fab.getLeft() + fab.getRight()) / 2;
            cy = (fab.getTop() + fab.getBottom()) / 2;
        }

        fab.hide();
        bottomPanel.setVisibility(VISIBLE);
        animateCircle(0, screenWidth, null);
    }

    public void showFab() {
        boolean isBarVisible = bottomPanel.getVisibility() == VISIBLE;
        if (!isBarVisible) {
            fab.show();
        }
    }

    public boolean hideBar() {
        boolean isBarVisible = bottomPanel.getVisibility() == VISIBLE;
        if (isBarVisible) {
            animatorListener.showFabOnAnimationEnd = true;
            animateCircle(screenWidth, 0, animatorListener);
            return true;
        }
        return false;
    }

    public void hideBarWithoutAnimation() {
        bottomPanel.setVisibility(GONE);
        fab.show();
    }

    public void hideFabBar() {
        bottomPanel.setVisibility(GONE);
        fab.hide();
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
            showBar();
        }
    }

    private class AnimatorListener implements SupportAnimator.AnimatorListener {
        private boolean showFabOnAnimationEnd;

        @Override
        public void onAnimationStart() {
        }

        @Override
        public void onAnimationEnd() {
            bottomPanel.setVisibility(GONE);
            if (showFabOnAnimationEnd) {
                fab.show();
            }
        }

        @Override
        public void onAnimationCancel() {
        }

        @Override
        public void onAnimationRepeat() {
        }
    }
}
