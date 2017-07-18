package ru.android.childdiary.presentation.testing.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.ViewPagerAdapter;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;
import ru.android.childdiary.utils.ui.WidgetsUtils;

public class TestChartActivity extends BaseMvpActivity {
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
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setupViewPager();
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(putArguments(new PhysicalChartFragment()), getString(R.string.test_chart_physical_tab));
        viewPagerAdapter.addFragment(putArguments(new MentalChartFragment()), getString(R.string.test_chart_mental_tab));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0, false);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        WidgetsUtils.setupTabLayoutFont(tabLayout);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        tabLayout.setBackgroundColor(ThemeUtils.getColorPrimary(this, getSex()));
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getChildIconForToolbar(this, child));
        setupToolbarTitle(child.getName());
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
}
