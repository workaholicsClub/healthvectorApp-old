package ru.android.childdiary.presentation.medical.fragments.visits;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorVisitActionListener;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorVisitAdapter;
import ru.android.childdiary.presentation.medical.edit.medicines.EditDoctorVisitActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class DoctorVisitsFragment extends AppPartitionFragment
        implements DoctorVisitsView, DoctorVisitActionListener {
    @InjectPresenter
    DoctorVisitsPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Getter
    private DoctorVisitAdapter adapter;
    private FabController fabController;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_medical_list;
    }

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DoctorVisitAdapter(getContext(), this, fabController);
        recyclerView.setAdapter(adapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        adapter.setSex(getSex());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FabController) {
            fabController = (FabController) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fabController = null;
    }

    @Override
    public void showChild(@NonNull Child child) {
        super.showChild(child);
        adapter.getSwipeManager().setFabController(child.getId() == null ? null : fabController);
    }

    @Override
    public void showDoctorVisits(@NonNull DoctorVisitsFilter filter, @NonNull List<DoctorVisit> doctorVisits) {
        adapter.setItems(doctorVisits);
    }

    @Override
    public void navigateToDoctorVisit(@NonNull DoctorVisit doctorVisit,
                                      @NonNull DoctorVisit defaultDoctorVisit) {
        Intent intent = EditDoctorVisitActivity.getIntent(getContext(), doctorVisit, defaultDoctorVisit);
        startActivity(intent);
    }

    @Override
    public void doctorVisitDeleted(@NonNull DoctorVisit doctorVisit) {
    }

    @Override
    public void askDeleteConnectedEventsOrNot(@NonNull DoctorVisit doctorVisit) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_doctor_visit_connected_events_or_not)
                .setPositiveButton(R.string.delete_only_doctor_visit,
                        (DialogInterface dialog, int which) -> presenter.deleteDoctorVisit(doctorVisit))
                .setNegativeButton(R.string.delete_doctor_visit_and_events,
                        (DialogInterface dialog, int which) -> presenter.deleteDoctorVisitAndConnectedEvents(doctorVisit))
                .show();
    }

    @Override
    public void delete(DoctorVisit item) {
        presenter.delete(item);
    }

    @Override
    public void edit(DoctorVisit item) {
        presenter.editDoctorVisit(item);
    }
}
