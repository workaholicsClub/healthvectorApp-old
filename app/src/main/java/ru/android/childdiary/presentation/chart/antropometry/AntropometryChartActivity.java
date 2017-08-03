package ru.android.childdiary.presentation.chart.antropometry;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;
import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.presentation.chart.antropometry.pages.HeightChartFragment;
import ru.android.childdiary.presentation.chart.antropometry.pages.WeightChartFragment;
import ru.android.childdiary.presentation.chart.core.ChartActivity;
import ru.android.childdiary.presentation.chart.core.ChartFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class AntropometryChartActivity extends ChartActivity {
    private static final String KEY_SELECTED_PAGE = "antropometry_chart.selected_page";

    public static Intent getIntent(Context context, @Nullable Child child) {
        return new Intent(context, AntropometryChartActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarLogo(ResourcesUtils.getChildIconForToolbar(this, getChild()));
        setupToolbarTitle(getChild().getName());
    }

    @Override
    public String getKeySelectedPage() {
        return KEY_SELECTED_PAGE;
    }

    @Override
    protected List<ChartFragment> getChartFragments() {
        return Arrays.asList(new WeightChartFragment(), new HeightChartFragment());
    }

    @Override
    protected List<Class<? extends ChartFragment>> getChartFragmentClasses() {
        return Arrays.asList(WeightChartFragment.class, HeightChartFragment.class);
    }

    @Override
    protected List<String> getTitles() {
        return Arrays.asList(getString(R.string.weight), getString(R.string.height));
    }
}
