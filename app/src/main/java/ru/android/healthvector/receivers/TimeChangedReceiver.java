package ru.android.healthvector.receivers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import ru.android.healthvector.app.ChildDiaryApplication;
import ru.android.healthvector.data.services.ServiceController;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.receivers.core.BaseReceiver;

public class TimeChangedReceiver extends BaseReceiver {
    @Inject
    ServiceController serviceController;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        ApplicationComponent applicationComponent = ChildDiaryApplication.getApplicationComponent();
        applicationComponent.inject(this);
        serviceController.onTimeChanged();
    }
}
