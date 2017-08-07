package ru.android.childdiary.presentation.dictionaries.achievements;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.achievements.data.Achievement;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.dictionaries.achievements.adapters.AchievementActionListener;
import ru.android.childdiary.presentation.dictionaries.achievements.adapters.AchievementAdapter;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddActivity;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddView;
import ru.android.childdiary.presentation.dictionaries.core.BasePickerActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class AchievementPickerActivity extends BasePickerActivity<Achievement, AchievementPickerView>
        implements AchievementPickerView, AchievementActionListener {
    @Getter
    @InjectPresenter
    AchievementPickerPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex, boolean pick) {
        return new Intent(context, AchievementPickerActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex)
                .putExtra(ExtraConstants.EXTRA_PICK, pick);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.medicines_title);
    }

    @Override
    public void delete(Achievement achievement) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_medicine_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> getPresenter().deleteItem(achievement))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void deletionRestricted() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.deletion_restricted_medicine)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void deletionRestrictedAchievement() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.restrict_delete_achievement_message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    protected Class<? extends BaseAddActivity<Achievement, ? extends BaseAddView<Achievement>>> getAddActivityClass() {
        return AchievementAddActivity.class;
    }

    @Override
    protected BaseRecyclerViewAdapter<Achievement, ? extends BaseRecyclerViewHolder<Achievement>> createAdapter() {
        return new AchievementAdapter(this, this, this);
    }

    @Override
    protected String getIntentionText(boolean isFiltering) {
        return (isFiltering ? getString(R.string.nothing_found) + "\n\n" : "")
                + getString(R.string.add_medicine_intention);
    }
}
