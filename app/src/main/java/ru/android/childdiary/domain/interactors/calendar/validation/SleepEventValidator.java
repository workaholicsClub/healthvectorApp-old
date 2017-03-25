package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.utils.EventHelper;
import ru.android.childdiary.utils.ObjectUtils;

public class SleepEventValidator extends Validator<SleepEvent, CalendarValidationResult> {
    private final Context context;
    private final CalendarInteractor calendarInteractor;

    @Inject
    public SleepEventValidator(Context context, CalendarInteractor calendarInteractor) {
        this.context = context;
        this.calendarInteractor = calendarInteractor;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull SleepEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        if (EventHelper.isTimerStarted(event)) {
            Long count = calendarInteractor.getSleepEventsWithTimer()
                    .firstOrError()
                    .flatMapObservable(Observable::fromIterable)
                    .filter(sleepEvent -> ObjectUtils.equals(sleepEvent.getChild().getId(), event.getChild().getId())
                            && !ObjectUtils.equals(sleepEvent.getId(), event.getId()))
                    .count()
                    .blockingGet();

            if (count > 0) {
                CalendarValidationResult result = new CalendarValidationResult();
                result.addMessage(context.getString(R.string.validate_event_sleep_timer_already_started));
                results.add(result);
            }
        }

        return results;
    }
}
