package ru.android.childdiary.domain.core;

import org.joda.time.DateTime;

import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public interface RepeatParametersContainer {
    DateTime getDateTime();

    RepeatParameters getRepeatParameters();
}
