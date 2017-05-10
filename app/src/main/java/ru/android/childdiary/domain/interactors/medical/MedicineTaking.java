package ru.android.childdiary.domain.interactors.medical;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

@Value
@Builder(toBuilder = true)
public class MedicineTaking implements Serializable {
    Long id;

    Child child;

    Medicine medicine;

    Double amount;

    MedicineMeasure medicineMeasure;

    RepeatParameters repeatParameters;

    DateTime dateTime;

    DateTime finishDateTime;

    Boolean exported;

    Integer notifyTimeInMinutes;

    String note;

    String imageFileName;
}
