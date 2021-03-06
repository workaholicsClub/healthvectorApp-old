package ru.android.healthvector.data.repositories.core.settings;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.healthvector.BuildConfig;
import ru.android.healthvector.domain.core.settings.SettingsRepository;

@Singleton
public class SettingsDataRepository implements SettingsRepository {
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_FINISH_TIME = "finish_time";
    private static final String KEY_ACCOUNT_NAME = "account_name";
    private static final String KEY_IS_CLOUD_SHOWN = "is_cloud_shown";
    private static final String KEY_IS_APP_INTRO_SHOWN = "is_app_intro_shown";
    private static final String KEY_LAST_CHECKED_DATE = "last_checked_date";

    private static final LocalTime DEFAULT_START_TIME = new LocalTime(8, 0);
    private static final LocalTime DEFAULT_FINISH_TIME = new LocalTime(22, 0);
    private static final String DEFAULT_ACCOUNT_NAME = "";

    private static final LocalTimeAdapter startTimeAdapter = new LocalTimeAdapter(DEFAULT_START_TIME);
    private static final LocalTimeAdapter finishTimeAdapter = new LocalTimeAdapter(DEFAULT_FINISH_TIME);

    private final RxSharedPreferences preferences;

    @Inject
    public SettingsDataRepository(RxSharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Observable<LocalTime> getStartTime() {
        return preferences.getObject(KEY_START_TIME, DEFAULT_START_TIME,
                startTimeAdapter)
                .asObservable();
    }

    @Override
    public void setStartTime(@NonNull LocalTime startTime) {
        preferences.getObject(KEY_START_TIME, startTimeAdapter).set(startTime);
    }

    @Override
    public Observable<LocalTime> getStartTimeOnce() {
        return getStartTime().first(DEFAULT_START_TIME).toObservable();
    }

    @Override
    public Observable<LocalTime> getFinishTime() {
        return preferences.getObject(KEY_FINISH_TIME, DEFAULT_FINISH_TIME,
                finishTimeAdapter)
                .asObservable();
    }

    @Override
    public void setFinishTime(@NonNull LocalTime finishTime) {
        preferences.getObject(KEY_FINISH_TIME, finishTimeAdapter).set(finishTime);
    }

    @Override
    public Observable<LocalTime> getFinishTimeOnce() {
        return getFinishTime().first(DEFAULT_FINISH_TIME).toObservable();
    }

    @Override
    public Observable<String> getAccountName() {
        return preferences.getString(KEY_ACCOUNT_NAME, DEFAULT_ACCOUNT_NAME).asObservable();
    }

    @Override
    public void setAccountName(@Nullable String accountName) {
        preferences.getString(KEY_ACCOUNT_NAME).set(accountName);
    }

    @Override
    public void removeAccount() {
        preferences.getString(KEY_ACCOUNT_NAME).delete();
    }

    @Override
    public Observable<String> getAccountNameOnce() {
        return getAccountName().first(DEFAULT_ACCOUNT_NAME).toObservable();
    }

    @Override
    public Observable<Boolean> getIsAppIntroShown() {
        return preferences.getBoolean(KEY_IS_APP_INTRO_SHOWN).asObservable()
                .map(value -> shown(value, BuildConfig.SHOW_APP_INTRO_ON_EACH_START));
    }

    @Override
    public void setIsAppIntroShown(boolean value) {
        preferences.getBoolean(KEY_IS_APP_INTRO_SHOWN).set(value);
    }

    @Override
    public Observable<Boolean> getIsAppIntroShownOnce() {
        return getIsAppIntroShown().first(false).toObservable();
    }

    @Override
    public Observable<Boolean> getIsCloudShown() {
        return preferences.getBoolean(KEY_IS_CLOUD_SHOWN).asObservable()
                .map(value -> shown(value, BuildConfig.SHOW_CLOUD_ON_EACH_START));
    }

    @Override
    public void setIsCloudShown(boolean value) {
        preferences.getBoolean(KEY_IS_CLOUD_SHOWN).set(value);
    }

    @Override
    public Observable<Boolean> getIsCloudShownOnce() {
        return getIsCloudShown().first(false).toObservable();
    }

    private boolean shown(boolean shown, boolean showEachTimeValue) {
        return shown && !showEachTimeValue;
    }

    @Override
    public Observable<LocalDate> getLastCheckedDate() {
        return preferences.getLong(KEY_LAST_CHECKED_DATE, LocalDate.now().toDate().getTime())
                .asObservable()
                .map(millis -> LocalDate.fromDateFields(new Date(millis)));
    }

    @Override
    public void setLastCheckedDate(@NonNull LocalDate date) {
        preferences.getLong(KEY_LAST_CHECKED_DATE, LocalDate.now().toDate().getTime()).set(date.toDate().getTime());
    }

    @Override
    public Observable<LocalDate> getLastCheckedDateOnce() {
        return getLastCheckedDate().first(LocalDate.now()).toObservable();
    }

    private static class LocalTimeAdapter implements Preference.Adapter<LocalTime> {
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");
        private final Logger logger = LoggerFactory.getLogger(toString());
        private final LocalTime defaultValue;

        public LocalTimeAdapter(LocalTime defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public LocalTime get(@NonNull String key, @NonNull SharedPreferences preferences) {
            String content = preferences.getString(key, null);
            if (TextUtils.isEmpty(content)) {
                return defaultValue;
            }
            try {
                return TIME_FORMATTER.parseLocalTime(content);
            } catch (Exception e) {
                logger.warn("failed to read time (key: " + key + ") from preferences", e);
                return defaultValue;
            }
        }

        @Override
        public void set(@NonNull String key, @NonNull LocalTime value, @NonNull SharedPreferences.Editor editor) {
            String content = TIME_FORMATTER.print(value);
            editor.putString(key, content).commit();
        }
    }
}
