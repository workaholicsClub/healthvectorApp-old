package ru.android.childdiary.presentation.calendar.adapters.events;

import ru.android.childdiary.domain.interactors.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.adapters.swipe.ItemActionListener;

public interface EventActionListener extends ItemActionListener<MasterEvent> {
    void move(MasterEvent event);

    void done(MasterEvent event);
}
