package ru.android.childdiary.presentation.medical.filter.visits;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tokenautocomplete.FilteredArrayAdapter;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsFilter;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorFilteredAdapter;
import ru.android.childdiary.presentation.medical.filter.core.MedicalFilterDialogFragment;

public class DoctorVisitFilterDialogFragment
        extends MedicalFilterDialogFragment<Doctor, DoctorVisitFilterDialogArguments>
        implements DoctorVisitFilterView {
    @InjectPresenter
    DoctorVisitFilterPresenter presenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_filter_doctor_visit;
    }

    @Override
    protected FilteredArrayAdapter<Doctor> createFilteredAdapter() {
        return new DoctorFilteredAdapter(getContext(), dialogArguments.getItems());
    }

    @Override
    protected void buildFilter(@NonNull List<Doctor> selectedItems,
                               @NonNull LocalDate fromDate,
                               @NonNull LocalDate toDate) {
        GetDoctorVisitsFilter filter = GetDoctorVisitsFilter.builder()
                .selectedItems(selectedItems)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        presenter.setFilter(filter);
    }
}
