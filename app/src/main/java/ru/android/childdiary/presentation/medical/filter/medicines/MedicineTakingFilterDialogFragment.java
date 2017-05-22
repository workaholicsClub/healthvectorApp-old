package ru.android.childdiary.presentation.medical.filter.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tokenautocomplete.FilteredArrayAdapter;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListFilter;
import ru.android.childdiary.presentation.medical.adapters.medicines.MedicineFilteredAdapter;
import ru.android.childdiary.presentation.medical.filter.core.MedicalFilterDialogFragment;

public class MedicineTakingFilterDialogFragment
        extends MedicalFilterDialogFragment<Medicine, MedicineTakingFilterDialogArguments>
        implements MedicineTakingFilterView {
    @InjectPresenter
    MedicineTakingFilterPresenter presenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_filter_medicine_taking;
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
