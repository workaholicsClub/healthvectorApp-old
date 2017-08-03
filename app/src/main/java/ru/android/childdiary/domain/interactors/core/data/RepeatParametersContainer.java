package ru.android.childdiary.domain.interactors.core.data;

import org.joda.time.DateTime;

import ru.android.childdiary.domain.interactors.calendar.data.core.RepeatParameters;

public interface RepeatParametersContainer {
    DateTime getDateTime();

    RepeatParameters getRepeatParameters();
}
