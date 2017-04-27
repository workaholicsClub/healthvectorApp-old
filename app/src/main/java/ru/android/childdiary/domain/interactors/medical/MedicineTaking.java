package ru.android.childdiary.domain.interactors.medical;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

@Value
@Builder(toBuilder = true)
public class MedicineTaking {
    Long id;

    Medicine medicine;
}
