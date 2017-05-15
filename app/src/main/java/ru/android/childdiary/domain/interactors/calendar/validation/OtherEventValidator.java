package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.validation.Validator;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.utils.TimeUtils;

public class OtherEventValidator extends Validator<OtherEvent, CalendarValidationResult> {
    private final Context context;

    @Inject
    public OtherEventValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull OtherEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        results.add(validateOtherEventName(event.getName()));

        if (TimeUtils.isStartTimeLessThanFinishTime(event.getDateTime(), event.getFinishDateTime())) {
            CalendarValidationResult result = new CalendarValidationResult();
            result.addMessage(context.getString(R.string.validate_start_finish_time));
            results.add(result);
        }

        return results;
    }

    private CalendarValidationResult validateOtherEventName(@Nullable String otherEventName) {
        CalendarValidationResult result = new CalendarValidationResult(CalendarFieldType.OTHER_EVENT_NAME);
        if (TextUtils.isEmpty(otherEventName)) {
            result.addMessage(context.getString(R.string.validate_event_other_title_empty));
        }
        return result;
    }
}
