package ru.android.childdiary.presentation.medical.fragments.medicines;

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
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.adapters.swipe.FabController;
import ru.android.childdiary.presentation.medical.adapters.medicines.MedicineTakingActionListener;
import ru.android.childdiary.presentation.medical.adapters.medicines.MedicineTakingAdapter;
import ru.android.childdiary.presentation.medical.edit.visits.EditMedicineTakingActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MedicineTakingListFragment extends AppPartitionFragment
        implements MedicineTakingListView, MedicineTakingActionListener {
    @InjectPresenter
    MedicineTakingListPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Getter
    private MedicineTakingAdapter adapter;
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
        adapter = new MedicineTakingAdapter(getContext(), this, fabController);
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
    public void showMedicineTakingList(@NonNull MedicineTakingListFilter filter, @NonNull List<MedicineTaking> medicineTakingList) {
        adapter.setItems(medicineTakingList);
    }

    @Override
    public void navigateToMedicineTaking(@NonNull MedicineTaking medicineTaking,
                                         @NonNull MedicineTaking defaultMedicineTaking) {
        Intent intent = EditMedicineTakingActivity.getIntent(getContext(), medicineTaking, defaultMedicineTaking);
        startActivity(intent);
    }

    @Override
    public void medicineTakingDeleted(@NonNull MedicineTaking medicineTaking) {
    }

    @Override
    public void askDeleteConnectedEventsOrNot(@NonNull MedicineTaking medicineTaking) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.ask_delete_medicine_taking_connected_events_or_not)
                .setPositiveButton(R.string.delete_only_medicine_taking,
                        (DialogInterface dialog, int which) -> presenter.deleteMedicineTaking(medicineTaking))
                .setNegativeButton(R.string.delete_medicine_taking_and_events,
                        (DialogInterface dialog, int which) -> presenter.deleteMedicineTakingAndConnectedEvents(medicineTaking))
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
}
