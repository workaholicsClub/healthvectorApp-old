package ru.android.childdiary.presentation.medical.partitions.visits;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindView;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorChipsAdapter;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorVisitActionListener;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorVisitAdapter;
import ru.android.childdiary.presentation.medical.edit.medicines.EditDoctorVisitActivity;
import ru.android.childdiary.presentation.medical.filter.visits.DoctorVisitFilterDialogArguments;
import ru.android.childdiary.presentation.medical.filter.visits.DoctorVisitFilterDialogFragment;
import ru.android.childdiary.presentation.medical.partitions.core.BaseMedicalDataFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class DoctorVisitsFragment extends BaseMedicalDataFragment
        implements DoctorVisitsView, DoctorVisitActionListener {
    private static final String TAG_PROGRESS_DIALOG_DELETING_EVENTS = "TAG_PROGRESS_DIALOG_DELETING_EVENTS";

    @InjectPresenter
    DoctorVisitsPresenter presenter;

    @BindView(R.id.textViewIntention)
    TextView textViewIntention;

    @BindView(R.id.recyclerViewChips)
    RecyclerView recyclerViewChips;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Getter
    private DoctorVisitAdapter adapter;
    private FabController fabController;
    private DoctorChipsAdapter doctorChipsAdapter;

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
        recyclerView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        textViewIntention.setText(R.string.add_doctor_visit);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewChips.setLayoutManager(flowLayoutManager);

        doctorChipsAdapter = new DoctorChipsAdapter(getContext());
        recyclerViewChips.setAdapter(doctorChipsAdapter);
        recyclerViewChips.setVisibility(View.GONE);
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
    public void showFilter() {
        presenter.requestFilterDialog();
    }

    @Override
    public void showFilterDialog(@NonNull DoctorVisitFilterDialogArguments dialogArguments) {
        DoctorVisitFilterDialogFragment fragment = new DoctorVisitFilterDialogFragment();
        fragment.showAllowingStateLoss(getActivity().getSupportFragmentManager(), TAG_FILTER,
                dialogArguments.toBuilder()
                        .sex(getSex())
                        .build());
    }

    @Override
    public void showDoctorVisitsState(@NonNull DoctorVisitsState doctorVisitsState) {
        logger.debug("showDoctorVisitsState: " + doctorVisitsState);

        Child child = doctorVisitsState.getChild();
        showChild(child);

        List<DoctorVisit> doctorVisits = doctorVisitsState.getDoctorVisits();
        adapter.setItems(doctorVisits);
        recyclerView.setVisibility(doctorVisits.isEmpty() ? View.GONE : View.VISIBLE);
        textViewIntention.setVisibility(doctorVisits.isEmpty() ? View.VISIBLE : View.GONE);
        adapter.setFabController(child.getId() == null ? null : fabController);

        List<Doctor> doctors = doctorVisitsState.getFilter().getSelectedItems();
        doctorChipsAdapter.setItems(doctors);
        recyclerViewChips.setVisibility(doctors.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void navigateToDoctorVisit(@NonNull DoctorVisit doctorVisit,
                                      @NonNull DoctorVisit defaultDoctorVisit,
                                      @Nullable LocalTime startTime,
                                      @Nullable LocalTime finishTime) {
        Intent intent = EditDoctorVisitActivity.getIntent(getContext(),
                doctorVisit, defaultDoctorVisit, startTime, finishTime);
        startActivity(intent);
    }

    @Override
    public void doctorVisitDeleted(@NonNull DoctorVisit doctorVisit) {
    }

    @Override
    public void confirmDeleteDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_doctor_visit_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (DialogInterface dialog, int which) -> presenter.deleteDoctorVisit(doctorVisit))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void askDeleteConnectedEventsOrNot(@NonNull DoctorVisit doctorVisit) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_doctor_visit_connected_events_or_not)
                .setPositiveButton(R.string.delete_only_doctor_visit,
                        (DialogInterface dialog, int which) -> presenter.deleteDoctorVisit(doctorVisit))
                .setNegativeButton(R.string.delete_doctor_visit_and_events,
                        (DialogInterface dialog, int which) -> presenter.deleteDoctorVisitWithConnectedEvents(doctorVisit))
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
}
