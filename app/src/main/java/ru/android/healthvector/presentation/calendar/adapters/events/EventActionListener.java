package ru.android.healthvector.presentation.calendar.adapters.events;

import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.presentation.core.adapters.swipe.ItemActionListener;

public interface EventActionListener extends ItemActionListener<MasterEvent> {
    void move(MasterEvent event);

    void done(MasterEvent event);
}
