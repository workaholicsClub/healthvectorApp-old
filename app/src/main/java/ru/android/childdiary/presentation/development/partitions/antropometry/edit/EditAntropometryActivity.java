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

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.antropometry.data.Antropometry;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class EditAntropometryActivity extends AntropometryActivity<EditAntropometryView>
        implements EditAntropometryView {
    @Getter
    @InjectPresenter
    EditAntropometryPresenter presenter;

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
        super.onCreate(savedInstanceState);
        buttonAdd.setVisibility(View.GONE);
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
    protected void upsert(@NonNull Antropometry antropometry) {
        presenter.update(antropometry);
    }
}
