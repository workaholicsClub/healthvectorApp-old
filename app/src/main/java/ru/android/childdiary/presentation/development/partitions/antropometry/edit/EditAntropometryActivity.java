package ru.android.childdiary.presentation.development.partitions.antropometry.edit;

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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryActivity;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class EditAntropometryActivity extends AntropometryActivity<EditAntropometryView>
        implements EditAntropometryView, DatePickerDialog.OnDateSetListener {
    @Getter
    @InjectPresenter
    EditAntropometryPresenter presenter;

    private Antropometry antropometry;

    public static Intent getIntent(Context context, @NonNull Child child, @NonNull Antropometry antropometry) {
        return new Intent(context, EditAntropometryActivity.class)
                .putExtra(ExtraConstants.EXTRA_CHILD, child)
                .putExtra(ExtraConstants.EXTRA_ITEM, antropometry);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        antropometry = (Antropometry) getIntent().getSerializableExtra(ExtraConstants.EXTRA_ITEM);
        super.onCreate(savedInstanceState);
        buttonAdd.setVisibility(View.GONE);

        if (savedInstanceState == null) {
            showAntropometry(antropometry);
        }
    }

    @Override
    public void updated(@NonNull Antropometry antropometry) {
        finish();
    }

    @Override
    public void deleted(@NonNull Antropometry antropometry) {
        finish();
    }

    @Override
    public void confirmDelete(@NonNull Antropometry antropometry) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_antropometry_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.forceDelete(antropometry))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_with_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.delete(antropometry);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void saveChangesOrExit() {
        Antropometry newAntropometry = buildAntropometry(antropometry.toBuilder());
        if (ObjectUtils.contentEquals(newAntropometry, antropometry)) {
            finish();
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (dialog, which) -> presenter.update(newAntropometry))
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> finish())
                .show();
    }
}
