package ru.android.childdiary.services;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.utils.ui.NotificationUtils;

class NotificationManager {
    private List<SleepEvent> events = new ArrayList<>();
    private Map<Long, NotificationCompat.Builder> notificationBuilders = new HashMap<>();

    public void updateNotifications(Context context, List<SleepEvent> newEvents) {
        boolean changed = events.removeAll(newEvents);
        if (changed) {
            for (SleepEvent event : events) {
                Long masterEventId = event.getMasterEventId();
                NotificationUtils.hideNotification(context, (int) (masterEventId % Integer.MAX_VALUE));
            }
        }
        events = new ArrayList<>(newEvents);
        updateNotifications(context);
    }

    public void updateNotifications(Context context) {
        for (SleepEvent event : events) {
            Long masterEventId = event.getMasterEventId();
            NotificationCompat.Builder builder = notificationBuilders.get(masterEventId);
            if (builder == null) {
                builder = NotificationUtils.buildNotification(context, event);
                notificationBuilders.put(masterEventId, builder);
            } else {
                NotificationUtils.updateNotification(context, builder, event);
            }
            NotificationUtils.showNotification(context, (int) (masterEventId % Integer.MAX_VALUE), builder);
        }
    }
}
