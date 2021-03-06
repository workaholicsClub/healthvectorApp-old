package ru.android.healthvector.presentation.core.events;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;

import butterknife.OnClick;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public abstract class BaseEditItemActivity<V extends BaseEditItemView<T>, T extends Serializable>
        extends BaseItemActivity<V, T> implements BaseEditItemView<T> {
    private static final String TAG_PROGRESS_DIALOG_DELETING_EVENTS = "TAG_PROGRESS_DIALOG_DELETING_EVENTS";

    protected T item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection unchecked
        item = (T) getIntent().getSerializableExtra(ExtraConstants.EXTRA_ITEM);

        buttonAdd.setVisibility(View.GONE);
        setup(item);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.toolbar_action_overflow));
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @OnClick(R.id.buttonAdd)
    void onButtonFinishClick() {
        T item = build();
        getPresenter().complete(item);
    }

    @Override
    public void updated(@NonNull T item, int count) {
        if (count > 0) {
            showToast(getResources().getQuantityString(R.plurals.numberOfAddedEvents, count, count));
        }
        finish();
    }

    @Override
    public void deleted(@NonNull T item) {
        finish();
    }

    @Override
    public void completed(@NonNull T item) {
        buttonAdd.setVisibility(View.GONE);
        finish();
    }

    @Override
    public void showDeletingEvents(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_DELETING_EVENTS,
                    getString(R.string.please_wait),
                    getString(R.string.events_deleting));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_DELETING_EVENTS);
        }
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
                .setPositiveButton(R.string.save,
                        (dialog, which) -> getPresenter().update(item))
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> finish())
                .show();
    }

    protected abstract BaseEditItemPresenter<V, T> getPresenter();

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
                getPresenter().delete(this.item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
