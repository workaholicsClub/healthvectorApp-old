package ru.android.childdiary.presentation.medical.partitions.medicines;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.medical.data.MedicineTaking;
import ru.android.childdiary.presentation.medical.filter.adapters.Chips;

@Value
@Builder
public class MedicineTakingListState {
    @NonNull
    Child child;
    @NonNull
    List<Chips> chips;
    @NonNull
    List<MedicineTaking> medicineTakingList;
}
