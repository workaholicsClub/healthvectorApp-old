package ru.android.healthvector.presentation.medical.partitions.medicines;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.joda.time.LocalTime;

import java.util.List;

import butterknife.BindColor;
import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.healthvector.presentation.medical.adapters.medicines.MedicineTakingActionListener;
import ru.android.healthvector.presentation.medical.adapters.medicines.MedicineTakingAdapter;
import ru.android.healthvector.presentation.medical.add.visits.AddMedicineTakingActivity;
import ru.android.healthvector.presentation.medical.edit.visits.EditMedicineTakingActivity;
import ru.android.healthvector.presentation.medical.filter.adapters.Chips;
import ru.android.healthvector.presentation.medical.filter.adapters.ChipsAdapter;
import ru.android.healthvector.presentation.medical.filter.medicines.MedicineTakingFilterDialogArguments;
import ru.android.healthvector.presentation.medical.filter.medicines.MedicineTakingFilterDialogFragment;
import ru.android.healthvector.presentation.medical.partitions.core.BaseMedicalDataFragment;
import ru.android.healthvector.utils.HtmlUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class MedicineTakingListFragment extends BaseMedicalDataFragment
        implements MedicineTakingListView, MedicineTakingActionListener, HtmlUtils.OnLinkClickListener {
    private static final String LINK_ADD = "add";

    @Getter
    @InjectPresenter
    MedicineTakingListPresenter presenter;

    @BindColor(R.color.intention_text)
    int intentionTextColor;

    @Getter
    private MedicineTakingAdapter adapter;

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MedicineTakingAdapter(getContext(), this, fabController);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        footer.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        String text = getString(R.string.link_format,
                LINK_ADD,
                getString(R.string.add_medicine_taking));
        HtmlUtils.setupClickableLinks(textViewIntention, text, this, intentionTextColor);
        textViewNothingFound.setVisibility(View.GONE);

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
    public void showFilterDialog(@NonNull MedicineTakingFilterDialogArguments dialogArguments) {
        MedicineTakingFilterDialogFragment fragment = new MedicineTakingFilterDialogFragment();
        fragment.showAllowingStateLoss(getChildFragmentManager(), TAG_FILTER,
                dialogArguments.toBuilder()
                        .sex(getSex())
                        .build());
    }

    @Override
    public void showMedicineTakingListState(@NonNull MedicineTakingListState medicineTakingListState) {
        logger.debug("showMedicineTakingListState: " + medicineTakingListState);

        progressBar.setVisibility(View.GONE);

        Child child = medicineTakingListState.getChild();
        showChild(child);

        List<MedicineTaking> medicineTakingList = medicineTakingListState.getMedicineTakingList();
        adapter.setItems(medicineTakingList);
        adapter.setFabController(fabController);
        recyclerView.setVisibility(medicineTakingList.isEmpty() ? View.GONE : View.VISIBLE);

        List<Chips> chips = medicineTakingListState.getChips();
        chipsAdapter.setItems(chips);
        recyclerViewChips.setVisibility(chips.isEmpty() ? View.GONE : View.VISIBLE);
        footer.setVisibility(medicineTakingList.isEmpty() ? View.VISIBLE : View.GONE);

        if (chips.isEmpty()) {
            textViewNothingFound.setVisibility(View.GONE);
            String text = getString(R.string.link_format,
                    LINK_ADD,
                    getString(R.string.add_medicine_taking));
            HtmlUtils.setupClickableLinks(textViewIntention, text, this, intentionTextColor);
            textViewIntention.setVisibility(medicineTakingList.isEmpty() ? View.VISIBLE : View.GONE);
        } else {
            textViewNothingFound.setVisibility(View.VISIBLE);
            textViewIntention.setText(null);
            textViewIntention.setVisibility(View.GONE);
        }

        line.setVisibility(medicineTakingList.isEmpty() && !chips.isEmpty() ? View.VISIBLE : View.GONE);
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
        if (getContext() == null) {
            logger.error("context is null");
            return;
        }
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_medicine_taking_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.deleteMedicineTaking(medicineTaking))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void askDeleteConnectedEventsOrNot(@NonNull MedicineTaking medicineTaking) {
        if (getContext() == null) {
            logger.error("context is null");
            return;
        }
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_medicine_taking_connected_events_or_not)
                .setPositiveButton(R.string.delete_only_medicine_taking,
                        (dialog, which) -> presenter.deleteMedicineTaking(medicineTaking))
                .setNegativeButton(R.string.delete_medicine_taking_and_events,
                        (dialog, which) -> presenter.deleteMedicineTakingWithConnectedEvents(medicineTaking))
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
    public void onLinkClick(String url) {
        if (LINK_ADD.equals(url)) {
            presenter.addMedicineTaking();
        }
    }

    @Override
    public void navigateToMedicineTakingAdd(@NonNull MedicineTaking defaultMedicineTaking,
                                            @Nullable LocalTime startTime,
                                            @Nullable LocalTime finishTime) {
        Intent intent = AddMedicineTakingActivity.getIntent(getContext(), defaultMedicineTaking,
                startTime, finishTime);
        startActivity(intent);
    }
}
