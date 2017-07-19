package ru.android.childdiary.presentation.testing.dynamic;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.childdiary.data.types.TestType;

public class MentalChartFragment extends DomanChartFragment {
    @Getter
    @InjectPresenter
    DomanChartPresenter presenter;

    @Override
    public TestType getTestType() {
        return TestType.DOMAN_MENTAL;
    }
}
