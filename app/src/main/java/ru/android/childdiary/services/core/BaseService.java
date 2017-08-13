package ru.android.childdiary.services.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;

public abstract class BaseService extends Service {
    protected final Logger logger = LoggerFactory.getLogger(toString());
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public final IBinder onBind(Intent intent) {
        logger.debug("onBind: " + intent);
        return getBinder();
    }

    @Nullable
    protected abstract IBinder getBinder();

    @Override
    public final void onCreate() {
        logger.debug("onCreate");

        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        onCreate(component);
    }

    protected abstract void onCreate(ApplicationComponent applicationComponent);

    @CallSuper
    @Override
    public void onDestroy() {
        logger.debug("onDestroy");
        compositeDisposable.dispose();
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        logger.debug("onStartCommand: " + intent);

        handleIntent(intent);

        return START_STICKY;
    }

    protected abstract void handleIntent(@Nullable Intent intent);

    protected final void unsubscribe(@Nullable Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    protected final Disposable unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
        return disposable;
    }
}
