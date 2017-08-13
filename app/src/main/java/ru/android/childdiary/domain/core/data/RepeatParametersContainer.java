package ru.android.childdiary.domain.core.data;

import org.joda.time.DateTime;

import ru.android.childdiary.domain.calendar.data.core.RepeatParameters;

public interface RepeatParametersContainer {
    DateTime getDateTime();

    RepeatParameters getRepeatParameters();
}
