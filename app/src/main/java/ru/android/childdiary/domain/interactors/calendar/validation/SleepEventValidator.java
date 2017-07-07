package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.domain.core.validation.Validator;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.GetSleepEventsResponse;
import ru.android.childdiary.utils.strings.EventUtils;

public class SleepEventValidator extends Validator<SleepEvent, CalendarValidationResult> {
    private final Context context;
    private final CalendarRepository calendarRepository;

    @Inject
    public SleepEventValidator(Context context, CalendarDataRepository calendarRepository) {
        this.context = context;
        this.calendarRepository = calendarRepository;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull SleepEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        if (EventUtils.isTimerStarted(event)) {
            Long count = calendarRepository.getSleepEvents(GetSleepEventsRequest.builder()
                    .child(event.getChild())
                    .withStartedTimer(true)
                    .build())
                    .map(GetSleepEventsResponse::getEvents)
                    .first(Collections.emptyList())
                    .flatMapObservable(Observable::fromIterable)
                    .filter(sleepEvent -> !EventUtils.sameEvent(sleepEvent, event))
                    .count()
                    .blockingGet();

            if (count > 0) {
                CalendarValidationResult result = new CalendarValidationResult();
                result.addMessage(context.getString(R.string.validate_event_sleep_timer_already_started));
                results.add(result);
            }
        }

        DateTime start = event.getDateTime();
        DateTime finish = event.getFinishDateTime();
        if (start != null && finish != null && finish.isBefore(start)) {
            CalendarValidationResult result = new CalendarValidationResult();
            result.addMessage(context.getString(R.string.validate_start_finish_time));
            results.add(result);
        }

        return results;
    }
}
