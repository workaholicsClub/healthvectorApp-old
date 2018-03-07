package ru.android.healthvector.presentation.development.partitions.antropometry;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.antropometry.data.Antropometry;

@Value
@Builder
public class AntropometryListState {
    @NonNull
    Child child;
    @NonNull
    List<Antropometry> antropometryList;
}
