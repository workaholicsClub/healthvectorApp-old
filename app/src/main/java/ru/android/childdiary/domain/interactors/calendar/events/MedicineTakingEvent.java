package ru.android.childdiary.domain.interactors.calendar.events;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupFieldType;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupItem;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicineTakingEvent extends MasterEvent implements ContentObject<MedicineTakingEvent>, LinearGroupItem<MedicineTakingEvent> {
    private static final MedicineTakingEvent NULL = MedicineTakingEvent.builder().build();

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
                                Integer linearGroup,
                                Long id,
                                MedicineTaking medicineTaking,
                                Medicine medicine,
                                Double amount,
                                MedicineMeasure medicineMeasure,
                                String imageFileName) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.medicineTaking = medicineTaking;
        this.medicine = medicine;
        this.amount = amount;
        this.medicineMeasure = medicineMeasure;
        this.imageFileName = imageFileName;
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull MedicineTakingEvent other) {
        return contentEquals(this, other)
                && ObjectUtils.contentEquals(getMedicine(), other.getMedicine())
                && ObjectUtils.equals(getAmount(), other.getAmount())
                && ObjectUtils.contentEquals(getMedicineMeasure(), other.getMedicineMeasure())
                && ObjectUtils.contentEquals(getImageFileName(), other.getImageFileName());
    }

    public List<LinearGroupFieldType> getChangedFields(@NonNull MedicineTakingEvent other) {
        List<LinearGroupFieldType> significantFields = new ArrayList<>(
                MasterEvent.getChangedFields(this, other));

        if (!ObjectUtils.contentEquals(getMedicine(), other.getMedicine())) {
            significantFields.add(LinearGroupFieldType.MEDICINE);
        }

        if (!ObjectUtils.equals(getAmount(), other.getAmount())) {
            significantFields.add(LinearGroupFieldType.AMOUNT);
        }

        if (!ObjectUtils.equals(getMedicineMeasure(), other.getMedicineMeasure())) {
            significantFields.add(LinearGroupFieldType.MEDICINE_MEASURE);
        }

        return significantFields;
    }
}
