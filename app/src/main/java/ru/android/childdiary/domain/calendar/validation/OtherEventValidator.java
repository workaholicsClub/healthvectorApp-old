package ru.android.childdiary.domain.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.core.validation.core.ValidationException;
import ru.android.childdiary.domain.core.validation.core.Validator;
import ru.android.childdiary.domain.calendar.data.standard.OtherEvent;

public class OtherEventValidator extends Validator<OtherEvent, CalendarValidationResult> {
    private final Context context;

    @Inject
    public OtherEventValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull OtherEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        CalendarValidationResult result;

        result = new CalendarValidationResult(CalendarFieldType.OTHER_EVENT_NAME);
        if (TextUtils.isEmpty(event.getName())) {
            result.addMessage(context.getString(R.string.enter_event_name));
        }
        results.add(result);

        DateTime start = event.getDateTime();
        DateTime finish = event.getFinishDateTime();
        if (start != null && finish != null && finish.isBefore(start)) {
            result = new CalendarValidationResult();
            result.addMessage(context.getString(R.string.validate_start_finish_time));
            results.add(result);
        }

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<CalendarValidationResult> results) {
        return new CalendarValidationException(results);
    }
}
