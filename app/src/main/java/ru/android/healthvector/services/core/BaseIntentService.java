package ru.android.healthvector.services.core;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.android.healthvector.app.ChildDiaryApplication;
import ru.android.healthvector.di.ApplicationComponent;

public abstract class BaseIntentService extends IntentService {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    public BaseIntentService(String name) {
        super(name);
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        logger.debug("onCreate");

        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        onCreate(component);
    }

    protected abstract void onCreate(ApplicationComponent applicationComponent);

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("onDestroy");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        logger.debug("onHandleIntent: " + intent);

        handleIntent(intent);
    }

    protected abstract void handleIntent(@Nullable Intent intent);
}
