package ru.android.childdiary.data.repositories.medical;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.core.ValueDataRepository;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsFilter;

@Singleton
public class DoctorVisitFilterDataRepository extends ValueDataRepository<GetDoctorVisitsFilter> {
    @Inject
    public DoctorVisitFilterDataRepository() {
    }

    @Override
    protected GetDoctorVisitsFilter getDefaultValue() {
        return GetDoctorVisitsFilter.builder()
                .selectedItems(Collections.emptyList())
                .fromDate(null)
                .toDate(null)
                .build();
    }
}
