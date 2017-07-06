package ru.android.childdiary.presentation.development.partitions.tests;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;

public class TestingFragment extends BaseDevelopmentDiaryFragment<TestingView> implements TestingView {
    @InjectPresenter
    TestingPresenter presenter;

    @Override
    protected void setupUi() {
    }

    @Override
    public SwipeViewAdapter getAdapter() {
        return null; // TODO
    }

    @Override
    public TestingPresenter getPresenter() {
        return presenter;
    }
}
