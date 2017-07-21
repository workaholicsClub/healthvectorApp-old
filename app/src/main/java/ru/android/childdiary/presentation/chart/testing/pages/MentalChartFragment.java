package ru.android.childdiary.presentation.chart.testing.pages;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.presentation.chart.testing.pages.core.DomanChartFragment;
import ru.android.childdiary.presentation.chart.testing.pages.core.DomanChartPresenter;

public class MentalChartFragment extends DomanChartFragment {
    @Getter
    @InjectPresenter
    DomanChartPresenter presenter;

    @Override
    public TestType getTestType() {
        return TestType.DOMAN_MENTAL;
    }
}
