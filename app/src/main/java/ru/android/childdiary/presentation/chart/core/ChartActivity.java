package ru.android.childdiary.presentation.chart.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import lombok.AccessLevel;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.chart.testing.pages.MentalChartFragment;
import ru.android.childdiary.presentation.chart.testing.pages.PhysicalChartFragment;
import ru.android.childdiary.presentation.chart.testing.pages.core.DomanChartFragment;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.ViewPagerAdapter;
import ru.android.childdiary.utils.strings.TestUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public abstract class ChartActivity extends BaseMvpActivity {
    @Inject
    RxSharedPreferences preferences;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;
    @Getter(AccessLevel.PROTECTED)
    private Child child;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setupViewPager();
    }

    private void setupViewPager() {
        Integer selectedPage = preferences.getInteger(getKeySelectedPage(), 0).get();
        selectedPage = selectedPage == null ? 0 : selectedPage;
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<ChartFragment> chartFragments = getChartFragments();
        List<String> titles = getTitles();
        for (int i = 0; i < chartFragments.size(); ++i) {
            String title = i < titles.size() ? titles.get(i) : null;
            viewPagerAdapter.addFragment(putArguments(chartFragments.get(i)), title);
        }
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(selectedPage, false);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        WidgetsUtils.setupTabLayoutFont(tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                preferences.getInteger(getKeySelectedPage()).set(position);
                DomanChartFragment fragment = getSelectedPage(position);
                if (fragment != null) {
                    setupToolbarTitle(TestUtils.toString(ChartActivity.this, fragment.getTestParameter()));
                } else {
                    logger.error("selected page is null");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(this, getSex()));
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    private Fragment putArguments(Fragment fragment) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ExtraConstants.EXTRA_CHILD, child);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Nullable
    protected DomanChartFragment getSelectedPage(int position) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) {
            return null;
        }
        for (Fragment fragment : fragments) {
            if (position == 0 && fragment instanceof PhysicalChartFragment) {
                return (PhysicalChartFragment) fragment;
            } else if (position == 1 && fragment instanceof MentalChartFragment) {
                return (MentalChartFragment) fragment;
            }
        }
        return null;
    }

    @Nullable
    protected DomanChartFragment getSelectedPage() {
        int position = viewPager.getCurrentItem();
        return getSelectedPage(position);
    }

    protected abstract String getKeySelectedPage();

    protected abstract List<ChartFragment> getChartFragments();

    protected abstract List<String> getTitles();
}
