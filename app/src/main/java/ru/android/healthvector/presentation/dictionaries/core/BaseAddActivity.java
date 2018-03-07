package ru.android.healthvector.presentation.dictionaries.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;
import ru.android.healthvector.R;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.fields.widgets.FieldEditTextWithImageAutocompleteView;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public abstract class BaseAddActivity<T, V extends BaseAddView<T>> extends BaseMvpActivity implements BaseAddView<T> {
    @BindView(R.id.buttonAdd)
    protected Button buttonAdd;

    @BindView(R.id.autoCompleteView)
    protected FieldEditTextWithImageAutocompleteView autoCompleteView;

    @State
    boolean isButtonDoneEnabled;

    private ViewGroup detailsView;

    private boolean isValidationStarted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            String defaultText = getIntent().getStringExtra(ExtraConstants.EXTRA_TEXT);
            autoCompleteView.setText(defaultText);
        }

        setupEditTextView(autoCompleteView);

        unsubscribeOnDestroy(getPresenter().listenForDoneButtonUpdate(autoCompleteView.textObservable()));
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        detailsView = ButterKnife.findById(this, R.id.detailsView);
        inflater.inflate(getContentLayoutResourceId(), detailsView);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboardAndClearFocus();
    }

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {
        getPresenter().add(buildItem());
    }

    @LayoutRes
    protected abstract int getContentLayoutResourceId();

    protected abstract BaseAddPresenter<T, V> getPresenter();

    protected abstract T buildItem();

    protected abstract FilteredArrayAdapter<String> createFilteredAdapter(@NonNull List<T> items);

    @Override
    public void itemAdded(@NonNull T item) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showList(@NonNull List<T> list) {
        autoCompleteView.setFilteredAdapter(createFilteredAdapter(list));
    }

    @Override
    public void setButtonDoneEnabled(boolean enabled) {
        isButtonDoneEnabled = enabled;
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), isButtonDoneEnabled));
    }

    @Override
    public void validationFailed() {
        if (!isValidationStarted) {
            isValidationStarted = true;
            unsubscribeOnDestroy(getPresenter().listenForFieldsUpdate(autoCompleteView.textObservable()));
        }
    }

    @Override
    public void showValidationErrorMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void nameValidated(boolean valid) {
        autoCompleteView.validated(valid);
    }

    @Override
    public void onBackPressed() {
        saveChangesOrExit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveChangesOrExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveChangesOrExit() {
        String text = autoCompleteView.getText();
        if (TextUtils.isEmpty(text)) {
            finish();
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (dialog, which) -> getPresenter().add(buildItem()))
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> finish())
                .show();
    }
}
