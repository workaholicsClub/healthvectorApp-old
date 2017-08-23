package ru.android.childdiary.presentation.development;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.development.antropometry.data.Antropometry;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.PagesAdapter;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.development.partitions.achievements.ConcreteAchievementsFragment;
import ru.android.childdiary.presentation.development.partitions.achievements.add.AddConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.antropometry.AntropometryListFragment;
import ru.android.childdiary.presentation.development.partitions.antropometry.add.AddAntropometryActivity;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;
import ru.android.childdiary.presentation.development.partitions.testing.TestResultsFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class DevelopmentDiaryFragment extends AppPartitionFragment implements DevelopmentDiaryView,
        FabController {
    private static final String KEY_SELECTED_PAGE = "development_diary.selected_page";

    @Inject
    RxSharedPreferences preferences;

    @InjectPresenter
    DevelopmentDiaryPresenter presenter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private PagesAdapter pagesAdapter;

    @Override
    protected void injectFragment(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_app_partition_with_tabs;
    }

    @Override
    protected void setupUi() {
        setupViewPager();
        hideFabBar();
    }

    private void setupViewPager() {
        Integer selectedPage = preferences.getInteger(KEY_SELECTED_PAGE, 0).get();
        selectedPage = selectedPage == null ? 0 : selectedPage;
        pagesAdapter = new PagesAdapter(getChildFragmentManager());
        pagesAdapter.addFragment(putArguments(new ConcreteAchievementsFragment()), getString(R.string.achievements));
        pagesAdapter.addFragment(putArguments(new TestResultsFragment()), getString(R.string.development_tab_title_testing));
        pagesAdapter.addFragment(putArguments(new AntropometryListFragment()), getString(R.string.development_tab_title_antropometry_list));
        viewPager.setAdapter(pagesAdapter);
        viewPager.setCurrentItem(selectedPage, false);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        WidgetsUtils.setupTabLayoutFont(tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                preferences.getInteger(KEY_SELECTED_PAGE).set(position);
                updateSwipeLayouts(position);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void updateSwipeLayouts(int position) {
        SwipeViewAdapter adapter = getSwipeViewAdapter(position);
        if (adapter != null) {
            adapter.updateFabState();
        } else {
            hideFabBar();
        }
    }

    private void closeAllItems(int position) {
        SwipeViewAdapter adapter = getSwipeViewAdapter(position);
        if (adapter != null) {
            adapter.closeAllItems();
        } else {
            hideFabBar();
        }
    }

    @Nullable
    private SwipeViewAdapter getSwipeViewAdapter(int position) {
        AppPartitionFragment fragment = getSelectedPage(position);
        return fragment instanceof BaseDevelopmentDiaryFragment
                ? ((BaseDevelopmentDiaryFragment) fragment).getAdapter()
                : null;
    }

    @Nullable
    private AppPartitionFragment getSelectedPage(int position) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments == null) {
            return null;
        }
        for (Fragment fragment : fragments) {
            if (position == 0 && fragment instanceof ConcreteAchievementsFragment) {
                return (ConcreteAchievementsFragment) fragment;
            } else if (position == 1 && fragment instanceof TestResultsFragment) {
                return (TestResultsFragment) fragment;
            } else if (position == 2 && fragment instanceof AntropometryListFragment) {
                return (AntropometryListFragment) fragment;
            }
        }
        return null;
    }

    @Nullable
    public AppPartitionFragment getSelectedPage() {
        int position = viewPager.getCurrentItem();
        return getSelectedPage(position);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(getContext(), getSex()));
        fab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtils.getColorAccent(getContext(), getSex())));
    }

    @Override
    public void onResume() {
        super.onResume();
        int position = viewPager.getCurrentItem();
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
        int position = viewPager.getCurrentItem();
        AppPartitionFragment fragment = getSelectedPage(position);
        if (fragment != null) {
            fragment.showFilter();
        } else {
            logger.error("selected page: " + position + "; partition is null");
        }
    }

    @Override
    public void navigateToAchievementAdd(@NonNull Child child, @NonNull ConcreteAchievement defaultConcreteAchievement) {
        Intent intent = AddConcreteAchievementActivity.getIntent(getContext(), child, defaultConcreteAchievement);
        startActivity(intent);
    }

    @Override
    public void navigateToAntropometryAdd(@NonNull Child child, @NonNull Antropometry antropometry) {
        Intent intent = AddAntropometryActivity.getIntent(getContext(), child, antropometry);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        int selectedPage = viewPager.getCurrentItem();
        switch (selectedPage) {
            case 0:
                presenter.addAchievement();
                break;
            case 2:
                presenter.addAntropometry();
                break;
        }
    }

    @Override
    public void showFab() {
        fab.show();
    }

    @Override
    public boolean hideBar() {
        return false;
    }

    @Override
    public void hideBarWithoutAnimation() {
    }

    @Override
    public void hideFabBar() {
        fab.hide();
    }
}
