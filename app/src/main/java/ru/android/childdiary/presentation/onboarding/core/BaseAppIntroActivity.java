package ru.android.childdiary.presentation.onboarding.core;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.adapters.SlidesAdapter;

public abstract class BaseAppIntroActivity extends BaseMvpActivity {
    private final List<ImageView> dotIndicators = new ArrayList<>();

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.buttonNext)
    Button buttonNext;

    @BindView(R.id.buttonSkip)
    Button buttonSkip;

    @BindView(R.id.dotIndicatorsContainer)
    LinearLayout dotIndicatorsContainer;

    @State
    int currentlySelectedItem;

    private SlidesAdapter slidesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);

        buttonNext.setOnClickListener(v -> onNextPressed());
        buttonSkip.setOnClickListener(v -> onSkipPressed());

        setupViewPager();
        initDotIndicators();
        updateDotIndicators();
        updateButtonNextTitle();
    }

    private void setupViewPager() {
        slidesAdapter = new SlidesAdapter(getSupportFragmentManager());
        for (Fragment slide : getSlides()) {
            slidesAdapter.addFragment(slide);
        }
        viewPager.setAdapter(slidesAdapter);
        viewPager.setCurrentItem(currentlySelectedItem, false);
        viewPager.setOffscreenPageLimit(getSlidesCount() - 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentlySelectedItem = position;
                updateDotIndicators();
                updateButtonNextTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initDotIndicators() {
        dotIndicators.clear();
        dotIndicatorsContainer.removeAllViews();
        for (int i = 0; i < getSlidesCount(); ++i) {
            ImageView dot = new ImageView(this);
            dotIndicators.add(dot);
            dotIndicatorsContainer.addView(dot);
        }
    }

    private void updateDotIndicators() {
        for (int i = 0; i < dotIndicators.size(); ++i) {
            boolean isCurrent = i == currentlySelectedItem;
            @DrawableRes int drawableId = isCurrent ? R.drawable.dot_indicator_selected : R.drawable.dot_indicator_normal;
            Drawable drawable = ContextCompat.getDrawable(this, drawableId);
            dotIndicators.get(i).setImageDrawable(drawable);
        }
    }

    private void updateButtonNextTitle() {
        boolean isLast = currentlySelectedItem == getSlidesCount() - 1;
        buttonNext.setText(isLast ? R.string.done : R.string.next);
    }

    private void onNextPressed() {
        int nextItem = currentlySelectedItem + 1;
        if (nextItem < getSlidesCount()) {
            viewPager.setCurrentItem(nextItem);
        } else {
            onDonePressed();
        }
    }

    protected abstract List<Fragment> getSlides();

    protected abstract int getSlidesCount();

    protected abstract void onSkipPressed();

    protected abstract void onDonePressed();
}
