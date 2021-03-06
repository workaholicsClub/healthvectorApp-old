package ru.android.healthvector.presentation.chart.testing;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.List;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.presentation.chart.core.ChartActivity;
import ru.android.healthvector.presentation.chart.core.ChartFragment;
import ru.android.healthvector.presentation.chart.testing.pages.MentalChartFragment;
import ru.android.healthvector.presentation.chart.testing.pages.PhysicalChartFragment;
import ru.android.healthvector.presentation.chart.testing.pages.core.DomanChartFragment;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.utils.strings.TestUtils;

public class TestChartActivity extends ChartActivity {
    private static final String KEY_SELECTED_PAGE = "test_chart.selected_page";

    public static Intent getIntent(Context context, @Nullable Child child) {
        return new Intent(context, TestChartActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                DomanChartFragment fragment = (DomanChartFragment) getSelectedPage();
                if (fragment != null) {
                    fragment.showFilter();
                    return true;
                } else {
                    logger.error("selected page is null");
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateTitle(@NonNull TestType testType, @NonNull DomanTestParameter parameter) {
        DomanChartFragment fragment = (DomanChartFragment) getSelectedPage();
        if (fragment == null) {
            logger.error("selected page is null");
            return;
        }
        if (fragment.getTestType() == testType) {
            setupToolbarTitle(TestUtils.testParameter(this, parameter));
        }
    }

    @Override
    public String getKeySelectedPage() {
        return KEY_SELECTED_PAGE;
    }

    @Override
    protected List<ChartFragment> getChartFragments() {
        return Arrays.asList(new PhysicalChartFragment(), new MentalChartFragment());
    }

    @Override
    protected List<Class<? extends ChartFragment>> getChartFragmentClasses() {
        return Arrays.asList(PhysicalChartFragment.class, MentalChartFragment.class);
    }

    @Override
    protected List<String> getTitles() {
        return Arrays.asList(getString(R.string.test_chart_physical_tab), getString(R.string.test_chart_mental_tab));
    }
}
