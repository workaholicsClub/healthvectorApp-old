package ru.android.childdiary.presentation.main.calendar.adapters.events;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

public interface EventActionListener {
    void delete(MasterEvent event);

    void move(MasterEvent event);

    void edit(MasterEvent event);

    void done(MasterEvent event);
}
