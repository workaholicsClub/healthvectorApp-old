package ru.android.childdiary.presentation.core.events;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialog;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDurationView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldRepeatParametersView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.presentation.core.widgets.CustomTimePickerDialog;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.TimeUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseAddItemActivity<V extends BaseAddItemView<T>, T extends Serializable>
        extends BaseMvpActivity implements BaseAddItemView<T>,
        FieldCheckBoxView.FieldCheckBoxListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TimeDialog.Listener {
    private static final String TAG_TIME_PICKER = "TIME_PICKER";
    private static final String TAG_DATE_PICKER = "DATE_PICKER";
    private static final String TAG_DURATION_DIALOG = "TAG_DURATION_DIALOG";
    private static final String TAG_NOTIFY_TIME_DIALOG = "TAG_NOTIFY_TIME_DIALOG";

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.dummy)
    View dummy;

    @BindView(R.id.buttonAdd)
    Button buttonAdd;

    private ViewGroup detailsView;

    protected T defaultItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //noinspection unchecked
        defaultItem = (T) getIntent().getSerializableExtra(ExtraConstants.EXTRA_DEFAULT_ITEM);

        setup(defaultItem);

        getCheckBoxView().setText(R.string.export_to_calendar);
        getCheckBoxView().setFieldCheckBoxListener(this);

        for (FieldEditTextView editTextView : getEditTextViews()) {
            setupEditTextView(editTextView);
        }

        getDateView().setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER, getDateView().getValue(), null, null));
        getTimeView().setFieldDialogListener(v -> showTimePicker(TAG_TIME_PICKER, getTimeView().getValue()));
        getNotifyTimeView().setFieldDialogListener(v -> getPresenter().requestTimeDialog(TAG_NOTIFY_TIME_DIALOG,
                TimeDialog.Parameters.builder()
                        .minutes(getNotifyTimeView().getValueInt())
                        .showDays(true)
                        .showHours(true)
                        .showMinutes(true)
                        .title(getString(R.string.notify_time_dialog_title))
                        .build()));
        if (getDurationView() != null) {
            getDurationView().setFieldDialogListener(v -> getPresenter().requestTimeDialog(TAG_DURATION_DIALOG,
                    TimeDialog.Parameters.builder()
                            .minutes(getDurationView().getValueInt())
                            .showDays(getDurationView().getValueInt() >= TimeUtils.MINUTES_IN_DAY)
                            .showHours(true)
                            .showMinutes(true)
                            .title(getString(R.string.duration))
                            .build()));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //noinspection unchecked
        T item = (T) savedInstanceState.getSerializable(ExtraConstants.EXTRA_ITEM);
        setup(item);
        dummy.requestFocus();
        logger.debug("restore: " + item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        T item = build();
        outState.putSerializable(ExtraConstants.EXTRA_ITEM, item);
        logger.debug("save: " + item);
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        detailsView = ButterKnife.findById(this, R.id.detailsView);
        View contentView = inflater.inflate(getContentLayoutResourceId(), detailsView);
    }

    public void hideKeyboardAndClearFocus(View view) {
        KeyboardUtils.hideKeyboard(this, view);
        view.clearFocus();
        dummy.requestFocus();
    }

    protected void setupEditTextView(FieldEditTextView view) {
        List<Disposable> disposables = view.createSubscriptions(this::hideKeyboardAndClearFocus);
        for (Disposable disposable : disposables) {
            unsubscribeOnDestroy(disposable);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        getCheckBoxView().setSex(getSex());
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {
        getPresenter().add(build());
    }

    @Override
    public void onChecked(boolean value) {
        // TODO
    }

    @Override
    public void added(@NonNull T item) {
        Toast.makeText(this, "added: " + item, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showTimeDialog(String tag, @NonNull Child child, TimeDialog.Parameters parameters) {
        TimeDialog dialog = new TimeDialog();
        dialog.showAllowingStateLoss(getSupportFragmentManager(), tag, child, parameters);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof TimePickerDialog) {
            ((TimePickerDialog) fragment).setOnTimeSetListener(this);
        } else if (fragment instanceof DatePickerDialog) {
            ((DatePickerDialog) fragment).setOnDateSetListener(this);
        }
    }

    protected void showDatePicker(String tag, @Nullable LocalDate date,
                                  @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        DatePickerDialog dpd = CustomDatePickerDialog.create(this, this, date, getSex(),
                minDate, maxDate);
        dpd.show(getFragmentManager(), tag);
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @Override
    public final void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        setDate(view.getTag(), date);
    }

    protected void setDate(String tag, LocalDate date) {
        getDateView().setValue(date);
    }

    protected void showTimePicker(String tag, @Nullable LocalTime time) {
        TimePickerDialog tpd = CustomTimePickerDialog.create(this, this, time, getSex());
        tpd.show(getFragmentManager(), tag);
        hideKeyboardAndClearFocus(rootView.findFocus());
    }

    @Override
    public final void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        LocalTime time = new LocalTime(hourOfDay, minute);
        setTime(view.getTag(), time);
    }

    protected void setTime(String tag, LocalTime time) {
        getTimeView().setValue(time);
    }

    @Override
    public void onSetTime(String tag, int minutes) {
        switch (tag) {
            case TAG_DURATION_DIALOG:
                if (getDurationView() != null) {
                    getDurationView().setValue(minutes);
                }
                break;
            case TAG_NOTIFY_TIME_DIALOG:
                getNotifyTimeView().setValue(minutes);
                break;
        }
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

    @LayoutRes
    protected abstract int getContentLayoutResourceId();

    protected abstract void setup(T item);

    protected abstract T build();

    protected abstract boolean contentEquals(T item1, T item2);

    protected abstract FieldDateView getDateView();

    protected abstract FieldTimeView getTimeView();

    protected abstract FieldNotifyTimeView getNotifyTimeView();

    @Nullable
    protected abstract FieldDurationView getDurationView();

    protected abstract FieldCheckBoxView getCheckBoxView();

    protected abstract FieldRepeatParametersView getRepeatParametersView();

    protected abstract List<FieldEditTextView> getEditTextViews();
}
