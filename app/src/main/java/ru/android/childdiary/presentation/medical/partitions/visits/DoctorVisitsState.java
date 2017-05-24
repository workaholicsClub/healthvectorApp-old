package ru.android.childdiary.presentation.medical.partitions.visits;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.medical.filter.adapters.Chips;

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
