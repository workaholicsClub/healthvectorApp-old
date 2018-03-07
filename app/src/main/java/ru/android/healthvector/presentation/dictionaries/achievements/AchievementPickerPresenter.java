package ru.android.healthvector.presentation.dictionaries.achievements;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.achievements.AchievementInteractor;
import ru.android.healthvector.domain.dictionaries.achievements.data.Achievement;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.healthvector.utils.strings.StringUtils;

@InjectViewState
public class AchievementPickerPresenter extends BasePickerPresenter<Achievement, AchievementPickerView> {
    @Getter
    @Inject
    AchievementInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected boolean filter(@NonNull Achievement item, @Nullable String filter) {
        return StringUtils.contains(item.getName(), filter, true);
    }
}
