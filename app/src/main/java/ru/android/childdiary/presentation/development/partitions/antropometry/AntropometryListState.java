package ru.android.childdiary.presentation.development.partitions.antropometry;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.antropometry.data.Antropometry;

@Value
@Builder
public class AntropometryListState {
    @NonNull
    Child child;
    @NonNull
    List<Antropometry> antropometryList;
}
