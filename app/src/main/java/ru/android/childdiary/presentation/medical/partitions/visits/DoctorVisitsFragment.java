package ru.android.childdiary.presentation.medical.partitions.visits;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.joda.time.LocalTime;

import java.util.List;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorVisitActionListener;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorVisitAdapter;
import ru.android.childdiary.presentation.medical.add.medicines.AddDoctorVisitActivity;
import ru.android.childdiary.presentation.medical.edit.medicines.EditDoctorVisitActivity;
import ru.android.childdiary.presentation.medical.filter.adapters.Chips;
import ru.android.childdiary.presentation.medical.filter.adapters.ChipsAdapter;
import ru.android.childdiary.presentation.medical.filter.visits.DoctorVisitFilterDialogArguments;
import ru.android.childdiary.presentation.medical.filter.visits.DoctorVisitFilterDialogFragment;
import ru.android.childdiary.presentation.medical.partitions.core.BaseMedicalDataFragment;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class DoctorVisitsFragment extends BaseMedicalDataFragment
        implements DoctorVisitsView, DoctorVisitActionListener, HtmlUtils.OnLinkClickListener {
    private static final String LINK_ADD = "add";

    @Getter
    @InjectPresenter
    DoctorVisitsPresenter presenter;

    @Getter
    private DoctorVisitAdapter adapter;

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoctorVisitAdapter(getContext(), this, fabController);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        String text = getString(R.string.link_format,
                LINK_ADD,
                getString(R.string.add_doctor_visit));
        HtmlUtils.setupClickableLinks(textViewIntention, text, this, ContextCompat.getColor(getContext(), R.color.intention_text));

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewChips.setLayoutManager(flowLayoutManager);

        chipsAdapter = new ChipsAdapter(getContext(), this);
        recyclerViewChips.setAdapter(chipsAdapter);
        recyclerViewChips.setVisibility(View.GONE);

        line.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.updateData();
    }

    @Override
    public void showFilterDialog(@NonNull DoctorVisitFilterDialogArguments dialogArguments) {
        DoctorVisitFilterDialogFragment fragment = new DoctorVisitFilterDialogFragment();
        fragment.showAllowingStateLoss(getChildFragmentManager(), TAG_FILTER,
                dialogArguments.toBuilder()
                        .sex(getSex())
                        .build());
    }

    @Override
    public void showDoctorVisitsState(@NonNull DoctorVisitsState doctorVisitsState) {
        logger.debug("showDoctorVisitsState: " + doctorVisitsState);

        progressBar.setVisibility(View.GONE);

        Child child = doctorVisitsState.getChild();
        showChild(child);

        List<DoctorVisit> doctorVisits = doctorVisitsState.getDoctorVisits();
        adapter.setItems(doctorVisits);
        adapter.setFabController(fabController);
        recyclerView.setVisibility(doctorVisits.isEmpty() ? View.GONE : View.VISIBLE);

        List<Chips> chips = doctorVisitsState.getChips();
        chipsAdapter.setItems(chips);
        recyclerViewChips.setVisibility(chips.isEmpty() ? View.GONE : View.VISIBLE);

        textViewIntention.setVisibility(doctorVisits.isEmpty() ? View.VISIBLE : View.GONE);
        if (chips.isEmpty()) {
            String text = getString(R.string.link_format,
                    LINK_ADD,
                    getString(R.string.add_doctor_visit));
            HtmlUtils.setupClickableLinks(textViewIntention, text, this, ContextCompat.getColor(getContext(), R.color.intention_text));
        } else {
            textViewIntention.setText(R.string.nothing_found);
        }

        line.setVisibility(doctorVisits.isEmpty() && !chips.isEmpty() ? View.VISIBLE : View.GONE);
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
                        (dialog, which) -> presenter.deleteDoctorVisit(doctorVisit))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void askDeleteConnectedEventsOrNot(@NonNull DoctorVisit doctorVisit) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_doctor_visit_connected_events_or_not)
                .setPositiveButton(R.string.delete_only_doctor_visit,
                        (dialog, which) -> presenter.deleteDoctorVisit(doctorVisit))
                .setNegativeButton(R.string.delete_doctor_visit_and_events,
                        (dialog, which) -> presenter.deleteDoctorVisitWithConnectedEvents(doctorVisit))
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
    public void onLinkClick(String url) {
        if (LINK_ADD.equals(url)) {
            presenter.addDoctorVisit();
        }
    }

    @Override
    public void navigateToDoctorVisitAdd(@NonNull DoctorVisit defaultDoctorVisit,
                                         @Nullable LocalTime startTime,
                                         @Nullable LocalTime finishTime) {
        Intent intent = AddDoctorVisitActivity.getIntent(getContext(), defaultDoctorVisit,
                startTime, finishTime);
        startActivity(intent);
    }
}
