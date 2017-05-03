package ru.android.childdiary.presentation.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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
import ru.android.childdiary.presentation.core.fields.widgets.FieldMedicineView;

public class AddMedicineTakingActivity extends BaseMvpActivity implements AddMedicineTakingView {
    @InjectPresenter
    AddMedicineTakingPresenter presenter;

    @BindView(R.id.medicineView)
    FieldMedicineView medicineView;

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
