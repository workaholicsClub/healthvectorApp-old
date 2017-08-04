package ru.android.childdiary.presentation.dictionaries.achievements;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.AchievementInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.presentation.dictionaries.core.BasePickerPresenter;
import ru.android.childdiary.utils.strings.StringUtils;

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
        return StringUtils.starts(item.getName(), filter, true);
    }
}
