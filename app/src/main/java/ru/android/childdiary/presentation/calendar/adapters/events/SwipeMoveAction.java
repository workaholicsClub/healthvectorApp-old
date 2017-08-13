package ru.android.childdiary.presentation.calendar.adapters.events;

import android.support.annotation.NonNull;

import com.daimajia.swipe.SwipeLayout;

import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.adapters.swipe.SwipeAction;

class SwipeMoveAction extends SwipeAction<MasterEvent, EventActionListener> {
    public SwipeMoveAction(@NonNull MasterEvent event, @NonNull EventActionListener listener) {
        super(event, listener);
    }

    @Override
    public void onClose(SwipeLayout layout) {
        layout.removeSwipeListener(this);
        listener.move(item);
    }
}
