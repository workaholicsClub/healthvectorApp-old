package ru.android.childdiary.presentation.development.partitions.achievements.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.OnClick;
import icepick.State;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.development.partitions.achievements.core.ConcreteAchievementActivity;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class AddConcreteAchievementActivity extends ConcreteAchievementActivity<AddConcreteAchievementView>
        implements AddConcreteAchievementView {
    @Getter
    @InjectPresenter
    AddConcreteAchievementPresenter presenter;

    @State
    boolean isButtonDoneEnabled;

    public static Intent getIntent(Context context, @NonNull Child child, @NonNull ConcreteAchievement defaultConcreteAchievement) {
        return new Intent(context, AddConcreteAchievementActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child)
                .putExtra(ExtraConstants.EXTRA_ITEM, defaultConcreteAchievement);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buttonAdd.setVisibility(View.VISIBLE);

        unsubscribeOnDestroy(getPresenter().listenForDoneButtonUpdate(
                achievementNameView.textObservable()
        ));
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {
        presenter.add(buildConcreteAchievement());
        if (isButtonDoneEnabled) {
            hideKeyboardAndClearFocus(rootView);
        }
    }

    @Override
    public void added(@NonNull ConcreteAchievement concreteAchievement) {
        finish();
    }

    @Override
    public void setButtonDoneEnabled(boolean enabled) {
        isButtonDoneEnabled = enabled;
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    protected void upsert(@NonNull ConcreteAchievement concreteAchievement) {
        presenter.add(concreteAchievement);
    }
}
