package ru.android.childdiary.domain.interactors.medical.data;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.data.ContentObject;
import ru.android.childdiary.domain.interactors.core.data.RepeatParametersContainer;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.calendar.data.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasure;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class MedicineTaking implements Serializable, RepeatParametersContainer,
        ContentObject<MedicineTaking> {
    private static final MedicineTaking NULL = MedicineTaking.builder().build();

    Long id;

    Child child;

    Medicine medicine;

    Double amount;

    MedicineMeasure medicineMeasure;

    RepeatParameters repeatParameters;

    DateTime dateTime;

    DateTime finishDateTime;

    Boolean isExported;

    Integer notifyTimeInMinutes;

    String note;

    String imageFileName;

    Boolean isDeleted;

    boolean isDone;

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull MedicineTaking other) {
        return ObjectUtils.contentEquals(getMedicine(), other.getMedicine())
                && ObjectUtils.equals(getAmount(), other.getAmount())
                && ObjectUtils.contentEquals(getMedicineMeasure(), other.getMedicineMeasure())
                && ObjectUtils.contentEquals(getRepeatParameters(), other.getRepeatParameters())
                && ObjectUtils.equalsToMinutes(getDateTime(), other.getDateTime())
                && ObjectUtils.equalsToMinutes(getFinishDateTime(), other.getFinishDateTime())
                && ObjectUtils.isTrue(getIsExported()) == ObjectUtils.isTrue(other.getIsExported())
                && ObjectUtils.equals(getNotifyTimeInMinutes(), other.getNotifyTimeInMinutes())
                && ObjectUtils.contentEquals(getNote(), other.getNote())
                && ObjectUtils.contentEquals(getImageFileName(), other.getImageFileName());
    }
}
