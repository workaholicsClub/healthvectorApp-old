package ru.android.healthvector.domain.core.data;

import org.joda.time.DateTime;

import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;

public interface RepeatParametersContainer {
    DateTime getDateTime();

    RepeatParameters getRepeatParameters();
}
