package ru.android.healthvector.data.repositories.medical;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.healthvector.data.repositories.core.ValueDataRepository;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListFilter;

@Singleton
public class MedicineTakingFilterDataRepository extends ValueDataRepository<GetMedicineTakingListFilter> {
    @Inject
    public MedicineTakingFilterDataRepository() {
    }

    @Override
    protected GetMedicineTakingListFilter getDefaultValue() {
        return GetMedicineTakingListFilter.builder()
                .selectedItems(Collections.emptyList())
                .fromDate(null)
                .toDate(null)
                .build();
    }
}
