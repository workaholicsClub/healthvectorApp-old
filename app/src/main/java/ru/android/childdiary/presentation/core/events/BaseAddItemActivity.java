package ru.android.childdiary.presentation.core.events;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.io.Serializable;

import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseAddItemActivity<V extends BaseAddItemView<T>, T extends Serializable>
        extends BaseItemActivity<V, T> implements BaseAddItemView<T> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonAdd.setVisibility(View.VISIBLE);
        setup(defaultItem);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
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
    public void setButtonAddEnabled(boolean enabled) {
        buttonAdd.setEnabled(enabled);
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
                .setPositiveButton(R.string.save_changes_dialog_positive_button_text,
                        (DialogInterface dialog, int which) -> getPresenter().add(item))
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }

    protected abstract BaseAddItemPresenter<V, T> getPresenter();
}
