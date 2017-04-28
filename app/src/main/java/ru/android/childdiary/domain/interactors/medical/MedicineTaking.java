package ru.android.childdiary.domain.interactors.medical;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

@Value
@Builder(toBuilder = true)
public class MedicineTaking {
    Long id;

    Medicine medicine;

    DateTime dateTime;

    Integer notifyTimeInMinutes;

    String note;

    String imageFileName;
}
