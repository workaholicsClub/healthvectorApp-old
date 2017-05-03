package ru.android.childdiary.domain.interactors.medical;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

@Value
@Builder(toBuilder = true)
public class MedicineTaking implements Serializable {
    Long id;

    Medicine medicine;

    Double amount;

    MedicineMeasure medicineMeasure;

    DateTime dateTime;

    Integer notifyTimeInMinutes;

    String note;

    String imageFileName;
}
