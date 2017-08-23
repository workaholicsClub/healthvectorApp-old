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
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.PagesAdapter;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public abstract class ChartActivity extends BaseMvpActivity implements ViewPager.OnPageChangeListener {
    @Inject
    RxSharedPreferences preferences;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private PagesAdapter pagesAdapter;
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
        pagesAdapter = new PagesAdapter(getSupportFragmentManager());
        List<ChartFragment> chartFragments = getChartFragments();
        List<String> titles = getTitles();
        for (int i = 0; i < chartFragments.size(); ++i) {
            String title = i < titles.size() ? titles.get(i) : null;
            pagesAdapter.addFragment(putArguments(chartFragments.get(i)), title);
        }
        viewPager.setAdapter(pagesAdapter);
        viewPager.setCurrentItem(selectedPage, false);
        viewPager.setOffscreenPageLimit(chartFragments.size() - 1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        WidgetsUtils.setupTabLayoutFont(tabLayout);
        viewPager.addOnPageChangeListener(this);
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
    protected ChartFragment getSelectedPage(int position) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) {
            return null;
        }
        List<Class<? extends ChartFragment>> chartFragmentClasses = getChartFragmentClasses();
        Class<? extends ChartFragment> cls = position >= 0 && position < chartFragmentClasses.size()
                ? chartFragmentClasses.get(position) : null;
        if (cls == null) {
            logger.error("chart fragment class is null");
            return null;
        }
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getClass() == cls) {
                return (ChartFragment) fragment;
            }
        }
        return null;
    }

    @Nullable
    public ChartFragment getSelectedPage() {
        int position = viewPager.getCurrentItem();
        return getSelectedPage(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        preferences.getInteger(getKeySelectedPage()).set(position);
        ChartFragment fragment = getSelectedPage(position);
        if (fragment != null) {
            fragment.setSelected();
        } else {
            logger.error("selected page is null");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    protected abstract String getKeySelectedPage();

    protected abstract List<ChartFragment> getChartFragments();

    protected abstract List<Class<? extends ChartFragment>> getChartFragmentClasses();

    protected abstract List<String> getTitles();
}
