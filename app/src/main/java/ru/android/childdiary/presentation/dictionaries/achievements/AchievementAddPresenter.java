package ru.android.childdiary.presentation.dictionaries.achievements;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.AchievementInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.domain.interactors.dictionaries.core.validation.DictionaryFieldType;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddPresenter;

@InjectViewState
public class AchievementAddPresenter extends BaseAddPresenter<Achievement, AchievementAddView> {
    @Getter
    @Inject
    AchievementInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.ACHIEVEMENT_NAME;
    }
}
