package ru.android.healthvector.domain.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.FeedType;
import ru.android.healthvector.domain.core.validation.core.ValidationException;
import ru.android.healthvector.domain.core.validation.core.Validator;
import ru.android.healthvector.domain.calendar.data.standard.FeedEvent;
import ru.android.healthvector.utils.ObjectUtils;

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
                    if (!ObjectUtils.isPositive(event.getLeftDurationInMinutes())
                            && ObjectUtils.isPositive(event.getRightDurationInMinutes())) {
                        CalendarValidationResult result = new CalendarValidationResult();
                        result.addMessage(context.getString(R.string.validate_event_feed_breast_milk_fill_left_duration));
                        results.add(result);
                    }
                    break;
                case RIGHT:
                    if (!ObjectUtils.isPositive(event.getRightDurationInMinutes())
                            && ObjectUtils.isPositive(event.getLeftDurationInMinutes())) {
                        CalendarValidationResult result = new CalendarValidationResult();
                        result.addMessage(context.getString(R.string.validate_event_feed_breast_milk_fill_right_duration));
                        results.add(result);
                    }
                    break;
            }
        }

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<CalendarValidationResult> results) {
        return new CalendarValidationException(results);
    }
}
