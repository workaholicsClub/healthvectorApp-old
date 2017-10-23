package ru.android.childdiary.utils.strings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.joda.time.DateTime;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.calendar.data.DoctorVisitEvent;
import ru.android.childdiary.domain.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.standard.DiaperEvent;
import ru.android.childdiary.domain.calendar.data.standard.FeedEvent;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;
import ru.android.childdiary.domain.calendar.data.standard.PumpEvent;
import ru.android.childdiary.domain.calendar.data.standard.SleepEvent;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.dictionaries.food.data.Food;
import ru.android.childdiary.domain.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.utils.ObjectUtils;

import static ru.android.childdiary.data.types.FeedType.BREAST_MILK;
import static ru.android.childdiary.data.types.FeedType.FOOD;

public class EventUtils {
    public static boolean canBeDone(@NonNull MasterEvent event) {
        return canBeDone(event.getEventType());
    }

    public static boolean canBeDone(@Nullable EventType eventType) {
        return eventType == EventType.OTHER
                || eventType == EventType.DOCTOR_VISIT
                || eventType == EventType.MEDICINE_TAKING
                || eventType == EventType.EXERCISE;
    }

    public static boolean isDone(@NonNull MasterEvent event) {
        return ObjectUtils.isTrue(event.getIsDone());
    }

    public static boolean isExpired(@NonNull MasterEvent event) {
        DateTime now = DateTime.now();
        return now.isAfter(event.getDateTime());
    }

    public static boolean isTimerStarted(@Nullable SleepEvent event) {
        return event != null && ObjectUtils.isTrue(event.getIsTimerStarted());
    }

    @Nullable
    public static String getTitle(Context context, @NonNull MasterEvent event) {
        EventType eventType = event.getEventType();
        String eventTypeStr = StringUtils.eventType(context, eventType);
        if (event instanceof OtherEvent) {
            OtherEvent otherEvent = (OtherEvent) event;
            return otherEvent.getName();
        } else if (event instanceof DoctorVisitEvent) {
            DoctorVisitEvent doctorVisitEvent = (DoctorVisitEvent) event;
            String name = doctorVisitEvent.getName();
            return TextUtils.isEmpty(name) ? eventTypeStr : name;
        } else if (event instanceof ExerciseEvent) {
            ExerciseEvent exerciseEvent = (ExerciseEvent) event;
            String name = exerciseEvent.getName();
            return TextUtils.isEmpty(name) ? eventTypeStr : name;
        }
        return eventTypeStr;
    }

    @Nullable
    public static String getDescription(Context context, @NonNull MasterEvent event) {
        if (event instanceof DiaperEvent) {
            DiaperEvent diaperEvent = (DiaperEvent) event;
            return StringUtils.diaperState(context, diaperEvent.getDiaperState());
        } else if (event instanceof FeedEvent) {
            FeedEvent feedEvent = (FeedEvent) event;
            FeedType feedType = feedEvent.getFeedType();
            if (feedType == BREAST_MILK) {
                return StringUtils.breast(context, feedEvent.getBreast());
            } else if (feedType == FOOD) {
                Food food = feedEvent.getFood();
                return food == null || food.getName() == null
                        ? context.getString(R.string.food) : food.getName();
            } else {
                return StringUtils.feedType(context, feedType);
            }
        } else if (event instanceof OtherEvent) {
            return null;
        } else if (event instanceof PumpEvent) {
            PumpEvent pumpEvent = (PumpEvent) event;
            return StringUtils.breast(context, pumpEvent.getBreast());
        } else if (event instanceof SleepEvent) {
            SleepEvent sleepEvent = (SleepEvent) event;
            if (EventUtils.isTimerStarted(sleepEvent)) {
                return TimeUtils.timerString(context, event.getDateTime(), DateTime.now());
            } else {
                return TimeUtils.durationShort(context, sleepEvent.getDateTime(), sleepEvent.getFinishDateTime());
            }
        } else if (event instanceof DoctorVisitEvent) {
            DoctorVisitEvent doctorVisitEvent = (DoctorVisitEvent) event;
            Doctor doctor = doctorVisitEvent.getDoctor();
            return doctor == null ? null : doctor.getName();
        } else if (event instanceof MedicineTakingEvent) {
            MedicineTakingEvent medicineTakingEvent = (MedicineTakingEvent) event;
            Medicine medicine = medicineTakingEvent.getMedicine();
            return medicine == null ? null : medicine.getName();
        } else if (event instanceof ExerciseEvent) {
            return null;
        }
        return null;
    }

    @Nullable
    public static String getTitleAndDescription(Context context, @NonNull MasterEvent event) {
        String title = getTitle(context, event);
        String description = getDescription(context, event);
        if (title == null && description == null) {
            return null;
        } else if (title != null && description == null) {
            return title;
        } else if (title == null) {
            return description;
        } else {
            return context.getString(R.string.event_title_description, title, description);
        }
    }

    public static boolean sameEvent(@Nullable MasterEvent event1, @Nullable MasterEvent event2) {
        return event1 != null && event2 != null
                && ObjectUtils.equals(event1.getMasterEventId(), event2.getMasterEventId());
    }
}
