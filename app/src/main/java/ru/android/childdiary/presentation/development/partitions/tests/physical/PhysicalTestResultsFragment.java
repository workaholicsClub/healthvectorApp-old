package ru.android.childdiary.presentation.development.partitions.tests.physical;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;

public class PhysicalTestResultsFragment extends BaseDevelopmentDiaryFragment<PhysicalTestResultsView> implements PhysicalTestResultsView {
    @InjectPresenter
    PhysicalTestResultsPresenter presenter;

    @Override
    protected void setupUi() {
    }

    @Override
    public SwipeViewAdapter getAdapter() {
        return null; // TODO
    }

    @Override
    public PhysicalTestResultsPresenter getPresenter() {
        return presenter;
    }
}
