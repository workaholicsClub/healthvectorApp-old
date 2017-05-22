package ru.android.childdiary.presentation.medical.partitions.medicines;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListFilter;

@Value
@Builder
public class MedicineTakingListState {
    @NonNull
    Child child;
    @NonNull
    GetMedicineTakingListFilter filter;
    @NonNull
    List<MedicineTaking> medicineTakingList;
}
