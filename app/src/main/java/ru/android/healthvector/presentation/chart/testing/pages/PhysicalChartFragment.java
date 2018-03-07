package ru.android.healthvector.presentation.chart.testing.pages;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.presentation.chart.testing.pages.core.DomanChartFragment;
import ru.android.healthvector.presentation.chart.testing.pages.core.DomanChartPresenter;

public class PhysicalChartFragment extends DomanChartFragment {
    @Getter
    @InjectPresenter
    DomanChartPresenter presenter;

    @Override
    public TestType getTestType() {
        return TestType.DOMAN_PHYSICAL;
    }
}
