package ru.android.childdiary.domain.interactors.calendar.events;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicineTakingEvent extends MasterEvent {
    Long id;

    MedicineTaking medicineTaking;

    Medicine medicine;

    Double amount;

    MedicineMeasure medicineMeasure;

    String imageFileName;

    @Builder(toBuilder = true)
    private MedicineTakingEvent(Long masterEventId,
                                EventType eventType,
                                DateTime dateTime,
                                Integer notifyTimeInMinutes,
                                String note,
                                Boolean isDone,
                                Child child,
                                RepeatParameters repeatParameters,
                                Integer linearGroup,
                                Long id,
                                MedicineTaking medicineTaking,
                                Medicine medicine,
                                Double amount,
                                MedicineMeasure medicineMeasure,
                                String imageFileName) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, repeatParameters, linearGroup);
        this.id = id;
        this.medicineTaking = medicineTaking;
        this.medicine = medicine;
        this.amount = amount;
        this.medicineMeasure = medicineMeasure;
        this.imageFileName = imageFileName;
    }
}
