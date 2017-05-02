package ru.android.childdiary.presentation.medical.fragments.visits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.core.swipe.FabController;
import ru.android.childdiary.presentation.core.swipe.ItemActionListener;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorVisitAdapter;

public class DoctorVisitsFragment extends AppPartitionFragment
        implements DoctorVisitsView, ItemActionListener<DoctorVisit> {
    @InjectPresenter
    DoctorVisitsPresenter presenter;

    @BindView(R.id.recyclerViewDoctorVisits)
    RecyclerView recyclerView;

    private DoctorVisitAdapter adapter;
    private FabController fabController;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_doctor_visits;
    }

    @Override
    protected void setupUi() {
        adapter = new DoctorVisitAdapter(getContext(), this, fabController);
        recyclerView.setAdapter(adapter);
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
    public void navigateToDoctorVisit() {
    }

    @Override
    public void delete(DoctorVisit item) {

    }

    @Override
    public void edit(DoctorVisit item) {

    }
}
