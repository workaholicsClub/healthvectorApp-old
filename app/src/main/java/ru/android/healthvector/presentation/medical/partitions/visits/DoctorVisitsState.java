package ru.android.healthvector.presentation.medical.partitions.visits;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.presentation.medical.filter.adapters.Chips;

@Value
@Builder
public class DoctorVisitsState {
    @NonNull
    Child child;
    @NonNull
    List<Chips> chips;
    @NonNull
    List<DoctorVisit> doctorVisits;
}
