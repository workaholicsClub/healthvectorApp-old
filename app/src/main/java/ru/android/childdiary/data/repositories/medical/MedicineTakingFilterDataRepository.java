package ru.android.childdiary.data.repositories.medical;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.android.childdiary.data.repositories.core.ValueDataRepository;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListFilter;

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
