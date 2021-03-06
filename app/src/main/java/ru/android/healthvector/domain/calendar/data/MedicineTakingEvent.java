package ru.android.healthvector.domain.calendar.data;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.data.core.LinearGroupFieldType;
import ru.android.healthvector.domain.calendar.data.core.LinearGroupItem;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.data.ContentObject;
import ru.android.healthvector.domain.core.data.RepeatParametersContainer;
import ru.android.healthvector.domain.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.utils.ObjectUtils;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicineTakingEvent extends MasterEvent implements ContentObject<MedicineTakingEvent>,
        LinearGroupItem<MedicineTakingEvent>, RepeatParametersContainer {
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
                                DateTime notifyDateTime,
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
        super(masterEventId, eventType, dateTime, notifyDateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
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

    @Override
    public RepeatParameters getRepeatParameters() {
        return medicineTaking.getRepeatParameters();
    }
}
