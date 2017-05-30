package ru.android.childdiary.presentation.development.partitions.antropometry;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;

public class AntropometryListFragment extends BaseDevelopmentDiaryFragment<AntropometryListView> implements AntropometryListView {
    @InjectPresenter
    AntropometryPresenter presenter;

    @Override
    protected void setupUi() {
    }

    @Override
    public SwipeViewAdapter getAdapter() {
        return null; // TODO
    }

    @Override
    public AntropometryPresenter getPresenter() {
        return presenter;
    }
}
