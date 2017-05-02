package ru.android.childdiary.presentation.medical.fragments.visits;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.core.AppPartitionFragment;

public class DoctorVisitsFragment extends AppPartitionFragment implements DoctorVisitsView {
    @InjectPresenter
    DoctorVisitsPresenter presenter;

    @BindView(R.id.recyclerViewVisits)
    RecyclerView recyclerViewVisits;

    //private DoctorVisitAdapter doctorVisitAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_doctor_visits;
    }

    @Override
    protected void setupUi() {
    }

    @Override
    public void showDoctorVisits(@NonNull DoctorVisitsFilter filter, @NonNull List<DoctorVisit> doctorVisits) {
    }

    @Override
    public void navigateToDoctorVisit() {
    }
}
