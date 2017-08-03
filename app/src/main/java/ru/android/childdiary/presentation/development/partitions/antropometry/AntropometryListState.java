package ru.android.childdiary.presentation.development.partitions.antropometry;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.data.Antropometry;

@Value
@Builder
public class AntropometryListState {
    @NonNull
    Child child;
    @NonNull
    List<Antropometry> antropometryList;
}
