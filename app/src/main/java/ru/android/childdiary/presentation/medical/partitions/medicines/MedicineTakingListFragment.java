package ru.android.childdiary.presentation.medical.partitions.medicines;

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
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.medical.adapters.medicines.MedicineChipsAdapter;
import ru.android.childdiary.presentation.medical.adapters.medicines.MedicineTakingActionListener;
import ru.android.childdiary.presentation.medical.adapters.medicines.MedicineTakingAdapter;
import ru.android.childdiary.presentation.medical.edit.visits.EditMedicineTakingActivity;
import ru.android.childdiary.presentation.medical.filter.medicines.MedicineTakingFilterDialogArguments;
import ru.android.childdiary.presentation.medical.filter.medicines.MedicineTakingFilterDialogFragment;
import ru.android.childdiary.presentation.medical.partitions.core.BaseMedicalDataFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MedicineTakingListFragment extends BaseMedicalDataFragment
        implements MedicineTakingListView, MedicineTakingActionListener {
    private static final String TAG_PROGRESS_DIALOG_DELETING_EVENTS = "TAG_PROGRESS_DIALOG_DELETING_EVENTS";

    @InjectPresenter
    MedicineTakingListPresenter presenter;

    @BindView(R.id.textViewIntention)
    TextView textViewIntention;

    @BindView(R.id.recyclerViewChips)
    RecyclerView recyclerViewChips;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Getter
    private MedicineTakingAdapter adapter;
    private FabController fabController;
    private MedicineChipsAdapter medicineChipsAdapter;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_medical_list;
    }

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MedicineTakingAdapter(getContext(), this, fabController);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        textViewIntention.setText(R.string.add_medicine_taking);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewChips.setLayoutManager(flowLayoutManager);

        medicineChipsAdapter = new MedicineChipsAdapter(getContext());
        recyclerViewChips.setAdapter(medicineChipsAdapter);
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
    public void showFilterDialog(@NonNull MedicineTakingFilterDialogArguments dialogArguments) {
        MedicineTakingFilterDialogFragment fragment = new MedicineTakingFilterDialogFragment();
        fragment.showAllowingStateLoss(getActivity().getSupportFragmentManager(), TAG_FILTER,
                dialogArguments.toBuilder()
                        .sex(getSex())
                        .build());
    }

    @Override
    public void showMedicineTakingListState(@NonNull MedicineTakingListState medicineTakingListState) {
        logger.debug("showMedicineTakingListState: " + medicineTakingListState);

        Child child = medicineTakingListState.getChild();
        showChild(child);

        List<MedicineTaking> medicineTakingList = medicineTakingListState.getMedicineTakingList();
        adapter.setItems(medicineTakingList);
        recyclerView.setVisibility(medicineTakingList.isEmpty() ? View.GONE : View.VISIBLE);
        textViewIntention.setVisibility(medicineTakingList.isEmpty() ? View.VISIBLE : View.GONE);
        adapter.setFabController(child.getId() == null ? null : fabController);

        List<Medicine> medicines = medicineTakingListState.getFilter().getSelectedItems();
        medicineChipsAdapter.setItems(medicines);
        recyclerViewChips.setVisibility(medicines.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void navigateToMedicineTaking(@NonNull MedicineTaking medicineTaking,
                                         @NonNull MedicineTaking defaultMedicineTaking,
                                         @Nullable LocalTime startTime,
                                         @Nullable LocalTime finishTime) {
        Intent intent = EditMedicineTakingActivity.getIntent(getContext(),
                medicineTaking, defaultMedicineTaking, startTime, finishTime);
        startActivity(intent);
    }

    @Override
    public void medicineTakingDeleted(@NonNull MedicineTaking medicineTaking) {
    }

    @Override
    public void confirmDeleteMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_medicine_taking_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (DialogInterface dialog, int which) -> presenter.deleteMedicineTaking(medicineTaking))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void askDeleteConnectedEventsOrNot(@NonNull MedicineTaking medicineTaking) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_medicine_taking_connected_events_or_not)
                .setPositiveButton(R.string.delete_only_medicine_taking,
                        (DialogInterface dialog, int which) -> presenter.deleteMedicineTaking(medicineTaking))
                .setNegativeButton(R.string.delete_medicine_taking_and_events,
                        (DialogInterface dialog, int which) -> presenter.deleteMedicineTakingWithConnectedEvents(medicineTaking))
                .show();
    }

    @Override
    public void delete(MedicineTaking item) {
        presenter.delete(item);
    }

    @Override
    public void edit(MedicineTaking item) {
        presenter.editMedicineTaking(item);
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
