package ru.android.childdiary.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.interactors.calendar.Food;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;

import static ru.android.childdiary.data.types.FeedType.BREAST_MILK;
import static ru.android.childdiary.data.types.FeedType.FOOD;

public class EventHelper {
    public static boolean canBeDone(@NonNull MasterEvent event) {
        return canBeDone(event.getEventType());
    }

    public static boolean canBeDone(@Nullable EventType eventType) {
        return eventType == EventType.OTHER;
    }

    public static boolean isDone(@NonNull MasterEvent event) {
        return event.getIsDone() != null && event.getIsDone();
    }

    public static boolean isExpired(@NonNull MasterEvent event) {
        DateTime now = DateTime.now();
        return now.isAfter(event.getDateTime());
    }

    public static boolean isTimerStarted(@Nullable SleepEvent event) {
        return event != null && event.getIsTimerStarted() != null && event.getIsTimerStarted();
    }

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
                        ? context.getString(R.string.feed_type_food) : food.getName();
            } else {
                return StringUtils.feedType(context, feedType);
            }
        } else if (event instanceof OtherEvent) {
            OtherEvent otherEvent = (OtherEvent) event;
            return otherEvent.getName();
        } else if (event instanceof PumpEvent) {
            PumpEvent pumpEvent = (PumpEvent) event;
            return StringUtils.breast(context, pumpEvent.getBreast());
        } else if (event instanceof SleepEvent) {
            SleepEvent sleepEvent = (SleepEvent) event;
            if (EventHelper.isTimerStarted(sleepEvent)) {
                return TimeUtils.timerString(context, event.getDateTime(), DateTime.now());
            } else {
                return TimeUtils.durationShort(context, sleepEvent.getDateTime(), sleepEvent.getFinishDateTime());
            }
        }
        throw new IllegalStateException("Unknown event type");
    }

    public static boolean sameEvent(@Nullable MasterEvent event1, @Nullable MasterEvent event2) {
        return event1 != null && event2 != null
                && ObjectUtils.equals(event1.getMasterEventId(), event2.getMasterEventId());
    }

    public static boolean sameChild(@Nullable MasterEvent event1, @Nullable MasterEvent event2) {
        return event1 != null && event2 != null
                && event1.getChild() != null && event2.getChild() != null
                && ObjectUtils.equals(event1.getChild().getId(), event2.getChild().getId());
    }

    public static boolean sameChild(@Nullable Child child, @Nullable MasterEvent event) {
        return child != null && event != null && event.getChild() != null
                && ObjectUtils.equals(child.getId(), event.getChild().getId());
    }
}
