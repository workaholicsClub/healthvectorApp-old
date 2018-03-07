package ru.android.healthvector.presentation.development.partitions.achievements.edit;

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
import ru.android.healthvector.R;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.development.partitions.achievements.core.ConcreteAchievementActivity;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

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

        boolean isPredefined = ObjectUtils.isTrue(getItem().getIsPredefined());
        if (isPredefined) {
            achievementNameView.setReadOnly(true);
            achievementNameView.removeInputFilters();
            achievementNameView.setText(getItem().getName());
        }
    }

    @Override
    public void updated(@NonNull ConcreteAchievement concreteAchievement) {
        setResult(RESULT_OK, new Intent().putExtra(ExtraConstants.EXTRA_ITEM, concreteAchievement));
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
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
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
