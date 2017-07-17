package ru.android.childdiary.presentation.testing.chart;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.childdiary.data.types.TestType;

public class MentalChartFragment extends DomanChartFragment {
    @Getter
    @InjectPresenter
    DomanChartPresenter presenter;

    @Override
    protected TestType getTestType() {
        return TestType.DOMAN_MENTAL;
    }
}
