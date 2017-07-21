package ru.android.childdiary.presentation.development.partitions.antropometry.core;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldHeightView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldWeightView;
import ru.android.childdiary.presentation.core.widgets.CustomDatePickerDialog;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public abstract class AntropometryActivity<V extends AntropometryView> extends BaseMvpActivity
        implements AntropometryView, DatePickerDialog.OnDateSetListener {
    private static final String TAG_DATE_PICKER = "DATE_PICKER";

    @BindView(R.id.buttonAdd)
    protected Button buttonAdd;

    @BindView(R.id.weightView)
    protected FieldWeightView weightView;

    @BindView(R.id.heightView)
    protected FieldHeightView heightView;

    @BindView(R.id.dateView)
    protected FieldDateView dateView;

    private Child child;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setupEditTextView(weightView);
        setupEditTextView(heightView);
        dateView.setFieldDialogListener(v -> showDatePicker(TAG_DATE_PICKER, dateView.getValue(),
                child.getBirthDate(), null));

        if (savedInstanceState == null) {
            getPresenter().init(child);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Antropometry antropometry = (Antropometry) savedInstanceState.getSerializable(ExtraConstants.EXTRA_ITEM);
        if (antropometry != null) {
            showAntropometry(antropometry);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExtraConstants.EXTRA_ITEM, buildAntropometry());
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        setupToolbarLogo(ResourcesUtils.getChildIconForToolbar(this, child));
        setupToolbarTitle(child.getName());
        buttonAdd.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(getSex(), true));
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup detailsView = ButterKnife.findById(this, R.id.detailsView);
        inflater.inflate(R.layout.activity_add_antropometry, detailsView);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DatePickerDialog) {
            ((DatePickerDialog) fragment).setOnDateSetListener(this);
        }
    }

    protected void setupEditTextView(FieldEditTextView view) {
        List<Disposable> disposables = view.createSubscriptions(this::hideKeyboardAndClearFocus);
        //noinspection Convert2streamapi
        for (Disposable disposable : disposables) {
            unsubscribeOnDestroy(disposable);
        }
    }

    protected void showDatePicker(String tag, @Nullable LocalDate date,
                                  @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        DatePickerDialog dpd = CustomDatePickerDialog.create(this, this, date, getSex(),
                minDate, maxDate);
        dpd.show(getFragmentManager(), tag);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        LocalDate date = LocalDate.fromCalendarFields(calendar);
        dateView.setValue(date);
    }

    protected abstract AntropometryPresenter<V> getPresenter();

    protected void showAntropometry(@NonNull Antropometry antropometry) {
        weightView.setValue(antropometry.getWeight());
        heightView.setValue(antropometry.getHeight());
        dateView.setValue(antropometry.getDate());
    }

    protected Antropometry buildAntropometry() {
        return buildAntropometry(Antropometry.builder());
    }

    protected Antropometry buildAntropometry(@NonNull Antropometry.AntropometryBuilder builder) {
        return builder
                .weight(weightView.getValue())
                .height(heightView.getValue())
                .date(dateView.getValue())
                .build();
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

    protected abstract void saveChangesOrExit();
}