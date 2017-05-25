package ru.android.childdiary.presentation.medical.pickers.core;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public abstract class BaseAddActivity<T, V extends BaseAddView<T>> extends BaseMvpActivity implements BaseAddView<T> {
    @BindView(R.id.rootView)
    protected View rootView;

    @BindView(R.id.imageView)
    protected ImageView imageView;

    @BindView(R.id.autoCompleteTextView)
    protected AutoCompleteTextView textView;

    @BindView(R.id.buttonAdd)
    protected Button buttonAdd;

    @BindDimen(R.dimen.name_edit_text_padding_bottom)
    int editTextBottomPadding;

    @State
    boolean isButtonDoneEnabled;

    private boolean isValidationStarted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_add);

        unsubscribeOnDestroy(RxTextView.editorActionEvents(textView).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE) {
                hideKeyboardAndClearFocus(textView);
            }
        }));
// TODO        textView.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);

        unsubscribeOnDestroy(getPresenter().listenForDoneButtonUpdate(RxTextView.afterTextChangeEvents(textView)));
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
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {
        getPresenter().add(buildItem());
    }

    protected abstract BaseAddPresenter<T, V> getPresenter();

    protected abstract T buildItem();

    protected abstract FilteredArrayAdapter<T> createFilteredAdapter(@NonNull List<T> items);

    @Override
    public void itemAdded(@NonNull T item) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showList(@NonNull List<T> list) {
        textView.setAdapter(createFilteredAdapter(list));
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
            unsubscribeOnDestroy(getPresenter().listenForFieldsUpdate(RxTextView.afterTextChangeEvents(textView)));
        }
    }

    @Override
    public void showValidationErrorMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void nameValidated(boolean valid) {
        viewValidated(textView, valid);
        textView.setPadding(0, 0, 0, editTextBottomPadding);
    }

    private void viewValidated(View view, boolean valid) {
        viewValidated(view, valid, R.drawable.edit_text_background, R.drawable.edit_text_background_error);
    }

    private void viewValidated(View view, boolean valid,
                               @DrawableRes int background, @DrawableRes int backgroundError) {
        view.setBackgroundResource(valid ? background : backgroundError);
    }
// TODO
    /*@Override
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
        if (ObjectUtils.isEmpty(editedChild)) {
            finish();
            return;
        }
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.save_changes_dialog_title)
                .setPositiveButton(R.string.save,
                        (DialogInterface dialog, int which) -> {
                                presenter.addChild(editedChild);
                        })
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }*/
}
