package ru.android.childdiary.presentation.development.partitions.tests.mental;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;

public class MentalTestResultsFragment extends BaseDevelopmentDiaryFragment<MentalTestResultsView> implements MentalTestResultsView {
    @InjectPresenter
    MentalTestResultsPresenter presenter;

    @Override
    protected void setupUi() {
    }

    @Override
    public SwipeViewAdapter getAdapter() {
        return null; // TODO
    }

    @Override
    public MentalTestResultsPresenter getPresenter() {
        return presenter;
    }
}
