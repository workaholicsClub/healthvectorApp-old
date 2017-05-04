package ru.android.childdiary.presentation.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDateView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldMedicineMeasureView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldMedicineView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNoteWithPhotoView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldNotifyTimeView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldRepeatParametersView;
import ru.android.childdiary.presentation.core.fields.widgets.FieldTimeView;

public class AddMedicineTakingActivity extends BaseMvpActivity implements AddMedicineTakingView {
    @InjectPresenter
    AddMedicineTakingPresenter presenter;

    @BindView(R.id.dummy)
    View dummy;

    @BindView(R.id.medicineView)
    FieldMedicineView medicineView;

    @BindView(R.id.medicineMeasureView)
    FieldMedicineMeasureView medicineMeasureView;

    @BindView(R.id.dateView)
    FieldDateView dateView;

    @BindView(R.id.timeView)
    FieldTimeView timeView;

    @BindView(R.id.repeatParametersView)
    FieldRepeatParametersView repeatParametersView;

    @BindView(R.id.noteWithPhotoView)
    FieldNoteWithPhotoView noteWithPhotoView;

    @BindView(R.id.notifyTimeView)
    FieldNotifyTimeView notifyTimeView;

    public static Intent getIntent(Context context) {
        return new Intent(context, AddMedicineTakingActivity.class);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_taking);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        // TODO checkbox color
    }

    @OnClick(R.id.buttonAdd)
    void onButtonAddClick() {
        presenter.addMedicineTaking(MedicineTaking.builder().build());
    }

    @Override
    public void showMedicines(List<Medicine> medicines) {
        medicineView.updateAdapter(medicines);
    }

    @Override
    public void showMedicineMeasureList(List<MedicineMeasure> medicineMeasureList) {
        // TODO
    }

    @Override
    public void medicineTakingAdded(@NonNull MedicineTaking medicineTaking) {
        Toast.makeText(this, "added: " + medicineTaking, Toast.LENGTH_SHORT).show();
    }
}
