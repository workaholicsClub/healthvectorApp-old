package ru.android.childdiary.presentation.medical.filter.visits;

import android.support.annotation.NonNull;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tokenautocomplete.FilteredArrayAdapter;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.medical.requests.GetDoctorVisitsFilter;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorFilteredAdapter;
import ru.android.childdiary.presentation.medical.filter.core.BaseTokenCompleteTextView;
import ru.android.childdiary.presentation.medical.filter.core.MedicalFilterDialogFragment;

public class DoctorVisitFilterDialogFragment
        extends MedicalFilterDialogFragment<Doctor, DoctorVisitFilterDialogArguments>
        implements DoctorVisitFilterView {
    @InjectPresenter
    DoctorVisitFilterPresenter presenter;

    @Override
    protected void setupUi() {
        super.setupUi();

        textViewByDoctor.setVisibility(View.VISIBLE);
        doctorTokenCompleteTextView.setVisibility(View.VISIBLE);
        textViewByMedicine.setVisibility(View.GONE);
        medicineTokenCompleteTextView.setVisibility(View.GONE);

        for (Doctor doctor : dialogArguments.getSelectedItems()) {
            getAutoCompleteTextView().addObject(doctor);
        }
    }

    @Override
    protected BaseTokenCompleteTextView<Doctor> getAutoCompleteTextView() {
        return doctorTokenCompleteTextView;
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
