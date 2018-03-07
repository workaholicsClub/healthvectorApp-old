package ru.android.healthvector.presentation.dictionaries.achievements;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.achievements.AchievementInteractor;
import ru.android.healthvector.domain.dictionaries.achievements.data.Achievement;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddPresenter;

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
