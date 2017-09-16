package ru.android.childdiary.presentation.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.presentation.calendar.partitions.BaseCalendarFragment;
import ru.android.childdiary.presentation.calendar.partitions.DayFragment;
import ru.android.childdiary.presentation.calendar.partitions.MonthFragment;
import ru.android.childdiary.presentation.calendar.partitions.WeekFragment;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.core.widgets.FabToolbar;
import ru.android.childdiary.presentation.events.DiaperEventDetailActivity;
import ru.android.childdiary.presentation.events.FeedEventDetailActivity;
import ru.android.childdiary.presentation.events.OtherEventDetailActivity;
import ru.android.childdiary.presentation.events.PumpEventDetailActivity;
import ru.android.childdiary.presentation.events.SleepEventDetailActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

import static android.support.v4.app.FragmentTransaction.TRANSIT_UNSET;

public class CalendarFragment extends AppPartitionFragment implements CalendarView,
        FabController {
    private static final String KEY_SELECTED_PAGE = "calendar.selected_page";
    private static final int REQUEST_ADD_EVENT = 1;

    @IdRes
    private static final int FRAGMENT_CONTAINER_ID = R.id.fragmentContainer;

    @Inject
    RxSharedPreferences preferences;

    @InjectPresenter
    CalendarPresenter presenter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.fabToolbar)
    FabToolbar fabToolbar;

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
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        setupViewPager();
        hideFabBar();
    }

    private void setupViewPager() {
        Integer selectedPage = preferences.getInteger(KEY_SELECTED_PAGE, 2).get();
        selectedPage = selectedPage == null ? 2 : selectedPage;
        tabLayout.addTab(tabLayout.newTab().setText(R.string.day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.week));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.month));
        TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedPage);
        if (selectedTab != null) {
            selectedTab.select();
        } else {
            logger.error("selected tab is null");
        }
        showPage(selectedPage);
        WidgetsUtils.setupTabLayoutFont(tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                preferences.getInteger(KEY_SELECTED_PAGE).set(position);
                showPage(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Nullable
    private String getTagForPosition(int position) {
        switch (position) {
            case 0:
                return DayFragment.class.getSimpleName();
            case 1:
                return WeekFragment.class.getSimpleName();
            case 2:
                return MonthFragment.class.getSimpleName();
        }
        return null;
    }

    private void showPage(int position) {
        String tag = getTagForPosition(position);
        Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = createFragmentAtPosition(position);
            logger.debug("fragment cache: create new fragment: " + fragment);
        } else {
            logger.debug("fragment cache: show fragment: " + fragment);
        }
        if (fragment == null) {
            logger.error("no fragment found for position " + position);
        }
        showFragment(fragment, tag);
    }

    @Nullable
    private Fragment createFragmentAtPosition(int position) {
        switch (position) {
            case 0:
                return putArguments(new DayFragment());
            case 1:
                return putArguments(new WeekFragment());
            case 2:
                return putArguments(new MonthFragment());
        }
        return null;
    }

    private void showFragment(Fragment fragment, String tag) {
        getChildFragmentManager()
                .beginTransaction()
                .setTransition(TRANSIT_UNSET)
                .replace(FRAGMENT_CONTAINER_ID, fragment, tag)
                .commit();
    }

    private void closeAllItems(int position) {
        SwipeViewAdapter adapter = getSwipeViewAdapter(position);
        if (adapter != null) {
            adapter.closeAllItems();
        } else {
            logger.error("selected page: " + position + "; event adapter is null");
        }
    }

    @Nullable
    private SwipeViewAdapter getSwipeViewAdapter(int position) {
        BaseCalendarFragment fragment = getSelectedPage(position);
        return fragment == null ? null : fragment.getEventAdapter();
    }

    @Nullable
    private BaseCalendarFragment getSelectedPage(int position) {
        String tag = getTagForPosition(position);
        Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
        if (fragment instanceof BaseCalendarFragment) {
            return (BaseCalendarFragment) fragment;
        }
        return null;
    }

    @Override
    public void themeChanged() {
        super.themeChanged();
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(getContext(), getSex()));
        fabToolbar.setColor(ThemeUtils.getColorAccent(getContext(), getSex()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EVENT && resultCode == Activity.RESULT_OK) {
            hideBarWithoutAnimation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int position = tabLayout.getSelectedTabPosition();
        closeAllItems(position);
    }

    @Override
    public void showChild(@NonNull Child child) {
        super.showChild(child);
        if (child.getId() == null) {
            hideFabBar();
        }
    }

    @Override
    public void showFilter() {
        int position = tabLayout.getSelectedTabPosition();
        BaseCalendarFragment fragment = getSelectedPage(position);
        if (fragment != null) {
            fragment.showFilter();
        } else {
            logger.error("selected page: " + position + "; partition is null");
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
    public void noChildSpecified() {
        showToast(getString(R.string.intention_add_child_profile));
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
}
