package ru.android.healthvector.app;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import net.danlew.android.joda.JodaTimeAndroid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import ru.android.healthvector.BuildConfig;
import ru.android.healthvector.R;
import ru.android.healthvector.data.services.ScheduleHelper;
import ru.android.healthvector.data.services.ServiceController;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.di.DaggerApplicationComponent;
import ru.android.healthvector.di.modules.ApplicationModule;
import ru.android.healthvector.utils.log.LogSystem;
import ru.android.healthvector.utils.ui.FontUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ChildDiaryApplication extends MultiDexApplication {
    @Getter
    private static ApplicationComponent applicationComponent;
    private Logger logger;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        // install uncaught exception handler before initializing crash report system
        Thread.setDefaultUncaughtExceptionHandler(
                new ChildDiaryUncaughtExceptionHandler(this, BuildConfig.DUMP_OUT_OF_MEMORY));

        LogSystem.initLogger(this);
        logger = LoggerFactory.getLogger(toString());
        logger.debug("onCreate");

        JodaTimeAndroid.init(this);

        ScheduleHelper scheduleHelper = new ScheduleHelper(this);
        ServiceController serviceController = new ServiceController(this, scheduleHelper);
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this, serviceController, scheduleHelper))
                .build();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(FontUtils.getFontPathRegular(this))
                .setFontAttrId(R.attr.fontPath)
                .build());

        serviceController.onApplicationStart();
    }

    /**
     * Получает счетчик {@link Tracker}, используемый по умолчанию для этого приложения {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Чтобы включить ведение журнала отладки, используйте adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
