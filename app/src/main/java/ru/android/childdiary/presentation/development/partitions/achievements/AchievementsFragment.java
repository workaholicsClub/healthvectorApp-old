package ru.android.childdiary.presentation.development.partitions.achievements;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.presentation.core.adapters.swipe.SwipeViewAdapter;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;

public class AchievementsFragment extends BaseDevelopmentDiaryFragment<AchievementsView> implements AchievementsView {
    @InjectPresenter
    AchievementsPresenter presenter;

    @Override
    protected void setupUi() {
    }

    @Override
    public SwipeViewAdapter getAdapter() {
        return null; // TODO
    }

    @Override
    public AchievementsPresenter getPresenter() {
        return presenter;
    }
}
