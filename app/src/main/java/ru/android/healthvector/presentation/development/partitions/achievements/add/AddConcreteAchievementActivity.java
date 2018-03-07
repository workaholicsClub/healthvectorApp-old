package ru.android.healthvector.presentation.development.partitions.achievements.add;

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
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.bindings.RxFieldValueView;
import ru.android.healthvector.presentation.development.partitions.achievements.core.ConcreteAchievementActivity;
import ru.android.healthvector.utils.ui.ResourcesUtils;

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
                achievementNameView.textObservable(),
                RxFieldValueView.valueChangeEvents(achievementTypeView)
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
            hideKeyboardAndClearFocus();
        }
    }

    @Override
    public void added(@NonNull ConcreteAchievement concreteAchievement) {
        setResult(RESULT_OK, new Intent().putExtra(ExtraConstants.EXTRA_ITEM, concreteAchievement));
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
