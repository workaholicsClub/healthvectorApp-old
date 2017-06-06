package ru.android.childdiary.app;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import net.danlew.android.joda.JodaTimeAndroid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.di.DaggerApplicationComponent;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.services.TimerService;
import ru.android.childdiary.services.UpdateDataService;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.ui.FontUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ChildDiaryApplication extends MultiDexApplication {
    @Getter
    private static ApplicationComponent applicationComponent;
    private Logger logger;

    @Override
    public void onCreate() {
        super.onCreate();

        // install uncaught exception handler before initializing crash report system
        Thread.setDefaultUncaughtExceptionHandler(
                new ChildDiaryUncaugthExceptionHandler(this, BuildConfig.DEBUG));

        LogSystem.initLogger(this);
        logger = LoggerFactory.getLogger(toString());
        logger.debug("onCreate");

        JodaTimeAndroid.init(this);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(FontUtils.getFontPathRegular(this))
                .setFontAttrId(R.attr.fontPath)
                .build());

        startService(new Intent(this, TimerService.class));
        startService(new Intent(this, UpdateDataService.class));
    }
}
