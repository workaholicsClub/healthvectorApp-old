package ru.android.childdiary.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.services.core.BaseService;

public class EventScheduleService extends BaseService {
    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, EventScheduleService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Nullable
    @Override
    protected IBinder getBinder() {
        return null;
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
        Toast.makeText(this, "event schedule", Toast.LENGTH_LONG).show();
        stopSelf();
    }
}
