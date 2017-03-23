package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.utils.ValueUtils;

public class FeedEventValidator extends Validator<FeedEvent, CalendarValidationResult> {
    private final Context context;

    @Inject
    public FeedEventValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull FeedEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();

        if (event.getFeedType() == FeedType.BREAST_MILK) {
            switch (event.getBreast()) {
                case LEFT:
                    if (!ValueUtils.hasValue(event.getLeftDurationInMinutes())
                            && ValueUtils.hasValue(event.getRightDurationInMinutes())) {
                        CalendarValidationResult result = new CalendarValidationResult();
                        result.addMessage(context.getString(R.string.validate_event_feed_breast_milk_fill_left_duration));
                        results.add(result);
                    }
                    break;
                case RIGHT:
                    if (!ValueUtils.hasValue(event.getRightDurationInMinutes())
                            && ValueUtils.hasValue(event.getLeftDurationInMinutes())) {
                        CalendarValidationResult result = new CalendarValidationResult();
                        result.addMessage(context.getString(R.string.validate_event_feed_breast_milk_fill_right_duration));
                        results.add(result);
                    }
                    break;
            }
        }

        return results;
    }
}
