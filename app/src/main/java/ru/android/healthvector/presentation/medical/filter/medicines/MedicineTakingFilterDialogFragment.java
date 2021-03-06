package ru.android.healthvector.presentation.medical.filter.medicines;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tokenautocomplete.FilteredArrayAdapter;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListFilter;
import ru.android.healthvector.presentation.medical.adapters.medicines.MedicineFilteredAdapter;
import ru.android.healthvector.presentation.medical.filter.core.BaseTokenCompleteTextView;
import ru.android.healthvector.presentation.medical.filter.core.MedicalFilterDialogFragment;

public class MedicineTakingFilterDialogFragment
        extends MedicalFilterDialogFragment<Medicine, MedicineTakingFilterDialogArguments>
        implements MedicineTakingFilterView {
    @InjectPresenter
    MedicineTakingFilterPresenter presenter;

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        super.setupUi(savedInstanceState);

        textViewByDoctor.setVisibility(View.GONE);
        doctorTokenCompleteTextView.setVisibility(View.GONE);
        textViewByMedicine.setVisibility(View.VISIBLE);
        medicineTokenCompleteTextView.setVisibility(View.VISIBLE);

        for (Medicine medicine : dialogArguments.getSelectedItems()) {
            getAutoCompleteTextView().addObject(medicine);
        }
    }

    @Override
    protected BaseTokenCompleteTextView<Medicine> getAutoCompleteTextView() {
        return medicineTokenCompleteTextView;
    }

    @Override
    protected FilteredArrayAdapter<Medicine> createFilteredAdapter() {
        return new MedicineFilteredAdapter(getContext(), dialogArguments.getItems());
    }

    @Override
    protected void buildFilter(@NonNull List<Medicine> selectedItems,
                               @NonNull LocalDate fromDate,
                               @NonNull LocalDate toDate) {
        GetMedicineTakingListFilter filter = GetMedicineTakingListFilter.builder()
                .selectedItems(selectedItems)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        presenter.setFilter(filter);
    }
}
