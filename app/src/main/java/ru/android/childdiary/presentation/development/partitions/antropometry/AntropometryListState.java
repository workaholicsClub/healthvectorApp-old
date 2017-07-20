package ru.android.childdiary.presentation.development.partitions.antropometry;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;

@Value
@Builder
public class AntropometryListState {
    @NonNull
    Child child;
    @NonNull
    List<Antropometry> antropometryList;
}
