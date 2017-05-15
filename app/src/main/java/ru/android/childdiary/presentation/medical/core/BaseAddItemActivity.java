package ru.android.childdiary.presentation.medical.core;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.io.Serializable;

import butterknife.OnClick;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseAddItemActivity<V extends BaseAddItemView<T>, T extends Serializable>
        extends BaseItemActivity<V, T> implements BaseAddItemView<T> {
    @State
    boolean isButtonDoneEnabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonAdd.setVisibility(View.VISIBLE);
        setup(defaultItem);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {
        getPresenter().add(build());
    }

    @Override
    public void added(@NonNull T item) {
        finish();
    }

    @Override
    public void setButtonDoneEnabled(boolean enabled) {
        isButtonDoneEnabled = enabled;
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    protected void saveChangesOrExit() {
        T item = build();
        if (contentEquals(item, defaultItem)) {
            finish();
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (DialogInterface dialog, int which) -> getPresenter().add(item))
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }

    protected abstract BaseAddItemPresenter<V, T> getPresenter();
}
