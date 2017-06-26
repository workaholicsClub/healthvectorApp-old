package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Comparator;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.domain.core.RepeatParametersContainer;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@Builder(toBuilder = true)
public class DoctorVisit implements Serializable, RepeatParametersContainer, ContentObject<DoctorVisit> {
    private static final DoctorVisit NULL = DoctorVisit.builder().build();

    Long id;

    Child child;

    Doctor doctor;

    RepeatParameters repeatParameters;

    String name;

    Integer durationInMinutes;

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
    public boolean isContentEqual(@NonNull DoctorVisit other) {
        return ObjectUtils.contentEquals(getDoctor(), other.getDoctor())
                && ObjectUtils.contentEquals(getRepeatParameters(), other.getRepeatParameters())
                && ObjectUtils.contentEquals(getName(), other.getName())
                && ObjectUtils.equals(getDurationInMinutes(), other.getDurationInMinutes())
                && ObjectUtils.equalsToMinutes(getDateTime(), other.getDateTime())
                && ObjectUtils.equalsToMinutes(getFinishDateTime(), other.getFinishDateTime())
                && ObjectUtils.isTrue(getIsExported()) == ObjectUtils.isTrue(other.getIsExported())
                && ObjectUtils.equals(getNotifyTimeInMinutes(), other.getNotifyTimeInMinutes())
                && ObjectUtils.contentEquals(getNote(), other.getNote())
                && ObjectUtils.contentEquals(getImageFileName(), other.getImageFileName());
    }

    /**
     * Компаратор для использования на уже отсортированном списке, который получаем из БД.
     * Воспользуемся тем фактом, что сортировка стабильная.
     */
    public static class DoneComparator implements Comparator<DoctorVisit> {
        @Override
        public int compare(DoctorVisit o1, DoctorVisit o2) {
            return o1.isDone == o2.isDone() ? 0 : (o1.isDone ? 1 : -1);
        }
    }
}
