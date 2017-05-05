package ru.android.childdiary.presentation.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.calendar.adapters.events.EventAdapter;
import ru.android.childdiary.presentation.calendar.fragments.BaseCalendarFragment;
import ru.android.childdiary.presentation.calendar.fragments.DayFragment;
import ru.android.childdiary.presentation.calendar.fragments.MonthFragment;
import ru.android.childdiary.presentation.calendar.fragments.WeekFragment;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.ViewPagerAdapter;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.presentation.main.widgets.FabToolbar;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class CalendarFragment extends AppPartitionFragment implements CalendarView,
        FabController {
    private static final String KEY_SELECTED_PAGE = "calendar.selected_page";
    private static final int REQUEST_ADD_EVENT = 1;

    @Inject
    RxSharedPreferences preferences;

    @InjectPresenter
    CalendarPresenter presenter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.fabToolbar)
    FabToolbar fabToolbar;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void injectFragment(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_calendar;
    }

    @Override
    protected void setupUi() {
        setupViewPager();
        hideFabBar();
    }

    private void setupViewPager() {
        Integer selectedPage = preferences.getInteger(KEY_SELECTED_PAGE, 2).get();
        selectedPage = selectedPage == null ? 2 : selectedPage;
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(putArguments(new DayFragment()), getString(R.string.day));
        viewPagerAdapter.addFragment(putArguments(new WeekFragment()), getString(R.string.week));
        viewPagerAdapter.addFragment(putArguments(new MonthFragment()), getString(R.string.month));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(selectedPage, false);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        WidgetsUtils.setupTabLayoutFont(tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                preferences.getInteger(KEY_SELECTED_PAGE).set(position);
                SwipeViewAdapter adapter = getSwipeListAdapter(position);
                if (adapter != null) {
                    adapter.getSwipeManager().update();
                } else {
                    logger.error("selected page: " + position + "; event adapter is null");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private Fragment putArguments(Fragment fragment) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ExtraConstants.EXTRA_CHILD, getChild());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void themeChanged() {
        super.themeChanged();
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(getContext(), getSex()));
        fabToolbar.setColor(ThemeUtils.getColorAccent(getContext(), getSex()));
    }

    @Override
    public void showChild(@NonNull Child child) {
        super.showChild(child);
        if (child.getId() == null) {
            hideFabBar();
        }
    }

    @OnClick(R.id.addDiaperEvent)
    void onAddDiaperEventClick() {
        presenter.addDiaperEvent();
    }

    @OnClick(R.id.addSleepEvent)
    void onAddSleepEventClick() {
        presenter.addSleepEvent();
    }

    @OnClick(R.id.addFeedEvent)
    void onAddFeedEventClick() {
        presenter.addFeedEvent();
    }

    @OnClick(R.id.addPumpEvent)
    void onAddPumpEventClick() {
        presenter.addPumpEvent();
    }

    @OnClick(R.id.addOtherEvent)
    void onAddOtherEventClick() {
        presenter.addOtherEvent();
    }

    @Override
    public void navigateToDiaperEventAdd(@NonNull DiaperEvent defaultEvent) {
        Intent intent = DiaperEventDetailActivity.getIntent(getContext(), null, defaultEvent);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void navigateToFeedEventAdd(@NonNull FeedEvent defaultEvent) {
        Intent intent = FeedEventDetailActivity.getIntent(getContext(), null, defaultEvent);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void navigateToOtherEventAdd(@NonNull OtherEvent defaultEvent) {
        Intent intent = OtherEventDetailActivity.getIntent(getContext(), null, defaultEvent);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void navigateToPumpEventAdd(@NonNull PumpEvent defaultEvent) {
        Intent intent = PumpEventDetailActivity.getIntent(getContext(), null, defaultEvent);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void navigateToSleepEventAdd(@NonNull SleepEvent defaultEvent) {
        Intent intent = SleepEventDetailActivity.getIntent(getContext(), null, defaultEvent);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EVENT && resultCode == Activity.RESULT_OK) {
            hideBarWithoutAnimation();
        }
    }

    @Override
    public void showFab() {
        fabToolbar.showFab();
    }

    @Override
    public boolean hideBar() {
        return fabToolbar.hideBar();
    }

    @Override
    public void hideBarWithoutAnimation() {
        fabToolbar.hideBarWithoutAnimation();
    }

    @Override
    public void hideFabBar() {
        fabToolbar.hideFabBar();
    }

    @Nullable
    private SwipeViewAdapter getSwipeListAdapter(int position) {
        BaseCalendarFragment fragment = (BaseCalendarFragment) viewPagerAdapter.getItem(position);
        EventAdapter eventAdapter = fragment.getEventAdapter();
        return eventAdapter;
    }
}
