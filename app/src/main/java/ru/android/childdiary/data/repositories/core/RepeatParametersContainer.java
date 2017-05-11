package ru.android.childdiary.data.repositories.core;

import org.joda.time.DateTime;

import ru.android.childdiary.domain.interactors.core.RepeatParameters;

public interface RepeatParametersContainer {
    DateTime getDateTime();

    RepeatParameters getRepeatParameters();
}
