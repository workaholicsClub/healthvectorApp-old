package ru.android.healthvector.presentation.medical.partitions.medicines;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.presentation.medical.filter.adapters.Chips;

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
