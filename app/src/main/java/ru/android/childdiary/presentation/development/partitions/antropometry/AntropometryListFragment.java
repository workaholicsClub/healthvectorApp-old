package ru.android.childdiary.presentation.development.partitions.antropometry;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;
import ru.android.childdiary.presentation.development.partitions.core.ChartContainer;

public class AntropometryListFragment extends BaseDevelopmentDiaryFragment<AntropometryListView>
        implements AntropometryListView, ChartContainer {
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

    @Override
    public void showChart() {
        showToast("chart");
    }
}
