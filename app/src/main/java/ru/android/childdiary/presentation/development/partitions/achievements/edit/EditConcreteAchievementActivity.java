package ru.android.childdiary.presentation.development.partitions.achievements.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.development.partitions.achievements.core.ConcreteAchievementActivity;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class EditConcreteAchievementActivity extends ConcreteAchievementActivity<EditConcreteAchievementView>
        implements EditConcreteAchievementView {
    @Getter
    @InjectPresenter
    EditConcreteAchievementPresenter presenter;

    public static Intent getIntent(Context context, @NonNull Child child, @NonNull ConcreteAchievement concreteAchievement) {
        return new Intent(context, EditConcreteAchievementActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child)
                .putExtra(ExtraConstants.EXTRA_ITEM, concreteAchievement);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buttonAdd.setVisibility(View.GONE);

        achievementNameView.setReadOnly(ObjectUtils.isTrue(getItem().getIsPredefined()));
    }

    @Override
    public void updated(@NonNull ConcreteAchievement concreteAchievement) {
        finish();
    }

    @Override
    public void deleted(@NonNull ConcreteAchievement concreteAchievement) {
        finish();
    }

    @Override
    public void confirmDelete(@NonNull ConcreteAchievement concreteAchievement) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_achievement_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.forceDelete(concreteAchievement))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ObjectUtils.isTrue(getItem().getIsPredefined())) {
            return super.onCreateOptionsMenu(menu);
        }
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_with_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.delete(getItem());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void upsert(@NonNull ConcreteAchievement concreteAchievement) {
        presenter.update(concreteAchievement);
    }
}
