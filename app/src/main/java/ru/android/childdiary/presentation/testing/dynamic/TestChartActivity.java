package ru.android.childdiary.presentation.testing.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.ViewPagerAdapter;
import ru.android.childdiary.utils.strings.TestUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class TestChartActivity extends BaseMvpActivity {
    private static final String KEY_SELECTED_PAGE = "chart.selected_page";

    @Inject
    RxSharedPreferences preferences;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;
    private Child child;

    public static Intent getIntent(Context context, @Nullable Child child) {
        return new Intent(context, TestChartActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setupViewPager();
    }

    private void setupViewPager() {
        Integer selectedPage = preferences.getInteger(KEY_SELECTED_PAGE, 0).get();
        selectedPage = selectedPage == null ? 0 : selectedPage;
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(putArguments(new PhysicalChartFragment()), getString(R.string.test_chart_physical_tab));
        viewPagerAdapter.addFragment(putArguments(new MentalChartFragment()), getString(R.string.test_chart_mental_tab));
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
                preferences.getInteger(KEY_SELECTED_PAGE).set(position);
                DomanChartFragment fragment = getSelectedPage(position);
                if (fragment != null) {
                    setupToolbarTitle(TestUtils.toString(TestChartActivity.this, fragment.getTestParameter()));
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
        setupToolbarTitle(null);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                DomanChartFragment fragment = getSelectedPage();
                if (fragment != null) {
                    fragment.showFilter();
                    return true;
                } else {
                    logger.error("selected page is null");
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    private DomanChartFragment getSelectedPage(int position) {
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
    private DomanChartFragment getSelectedPage() {
        int position = viewPager.getCurrentItem();
        return getSelectedPage(position);
    }

    public void updateTitle(@NonNull TestType testType, @NonNull DomanTestParameter parameter) {
        DomanChartFragment fragment = getSelectedPage();
        if (fragment == null) {
            logger.error("selected page is null");
            return;
        }
        if (fragment.getTestType() == testType) {
            setupToolbarTitle(TestUtils.toString(this, parameter));
        }
    }
}
