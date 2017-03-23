package ru.android.childdiary.domain.interactors.calendar.validation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.android.childdiary.domain.core.Validator;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;

public class SleepEventValidator extends Validator<SleepEvent, CalendarValidationResult> {
    private final Context context;

    @Inject
    public SleepEventValidator(Context context) {
        this.context = context;
    }

    @Override
    public List<CalendarValidationResult> validate(@NonNull SleepEvent event) {
        List<CalendarValidationResult> results = new ArrayList<>();
        return results;
    }
}
