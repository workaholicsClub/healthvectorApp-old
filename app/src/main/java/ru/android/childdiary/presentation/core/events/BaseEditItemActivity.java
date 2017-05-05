package ru.android.childdiary.presentation.core.events;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseEditItemActivity<V extends BaseEditItemView<T>, T extends Serializable>
        extends BaseItemActivity<V, T> implements BaseEditItemView<T> {
    @BindView(R.id.buttonFinish)
    Button buttonFinish;

    protected T item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        //noinspection unchecked
        item = (T) getIntent().getSerializableExtra(ExtraConstants.EXTRA_ITEM);

        setup(item);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonFinish.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @OnClick(R.id.buttonFinish)
    void onButtonFinishClick() {
        // TODO
        getPresenter().update(build());
    }

    @Override
    public void updated(@NonNull T item) {
        Toast.makeText(this, "updated: " + item, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void saveChangesOrExit() {
        T item = build();
        if (contentEquals(item, this.item)) {
            finish();
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save_changes_dialog_positive_button_text,
                        (DialogInterface dialog, int which) -> getPresenter().update(item))
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_item_edit;
    }

    protected abstract BaseEditItemPresenter<V, T> getPresenter();
}
