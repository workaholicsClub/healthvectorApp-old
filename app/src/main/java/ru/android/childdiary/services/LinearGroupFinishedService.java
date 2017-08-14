package ru.android.childdiary.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.services.core.BaseService;

public class LinearGroupFinishedService extends BaseService {
    private static Intent getServiceIntent(Context context) {
        return new Intent(context, LinearGroupFinishedService.class);
    }

    public static void startService(Context context) {
        Intent intent = getServiceIntent(context);
        context.startService(intent);
    }

    public static PendingIntent getPendingIntent(int requestCode, Context context) {
        Intent intent = getServiceIntent(context);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        Toast.makeText(this, "linear group finished", Toast.LENGTH_LONG).show();
        stopSelf();
    }
}
