package ru.android.childdiary.presentation.onboarding.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.adapters.SlidesAdapter;

public abstract class BaseAppIntroActivity extends BaseMvpActivity {
    protected SlidesAdapter slidesAdapter;
    protected IndicatorController indicatorController;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.skip)
    Button skipButton;

    @BindView(R.id.next)
    Button nextButton;

    @BindView(R.id.indicator_container)
    LinearLayout indicatorContainer;

    @State
    int currentlySelectedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);

        slidesAdapter = new SlidesAdapter(getSupportFragmentManager());

        skipButton.setOnClickListener(v -> onSkipPressed(slidesAdapter.getItem(viewPager.getCurrentItem())));
        nextButton.setOnClickListener(v -> changeSlide());

        List<Fragment> slides = getSlides();
        for (Fragment slide : slides) {
            slidesAdapter.addFragment(slide);
        }
        viewPager.setAdapter(slidesAdapter);
        viewPager.setCurrentItem(currentlySelectedItem, false);
        // TODO indicator
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicatorController.selectPosition(position);
                currentlySelectedItem = position;
                int count = slidesAdapter.getCount();
                int item = viewPager.getCurrentItem();
                boolean isLast = item == count - 1;
                nextButton.setText(isLast ? R.string.done : R.string.next);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        indicatorController = new IndicatorController();
        indicatorController.initialize(indicatorContainer, slidesAdapter.getCount());
        indicatorController.selectPosition(currentlySelectedItem);
    }

    public void onSkipPressed(Fragment currentFragment) {
    }

    public void addSlide(@NonNull Fragment fragment) {
        slidesAdapter.addFragment(fragment);
        viewPager.setOffscreenPageLimit(slidesAdapter.getCount() - 1);
    }

    public void onDonePressed(Fragment currentFragment) {
    }

    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
    }

    private void changeSlide() {
        int count = slidesAdapter.getCount();
        int nextItem = viewPager.getCurrentItem() + 1;
        if (nextItem < count) {
            viewPager.setCurrentItem(nextItem);
        } else {
            finish();
        }
    }

    protected abstract List<Fragment> getSlides();
}
