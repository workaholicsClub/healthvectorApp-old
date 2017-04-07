package ru.android.childdiary.presentation.calendar.adapters.events;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

public interface EventActionListener {
    void done(MasterEvent event);

    void move(MasterEvent event);

    void edit(MasterEvent event);

    void delete(MasterEvent event);
}
