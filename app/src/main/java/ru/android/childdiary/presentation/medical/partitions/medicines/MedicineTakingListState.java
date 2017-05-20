package ru.android.childdiary.presentation.medical.partitions.medicines;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

@Value
@Builder
public class MedicineTakingListState {
    @NonNull
    Child child;
    @NonNull
    List<MedicineTaking> medicineTakingList;
}
