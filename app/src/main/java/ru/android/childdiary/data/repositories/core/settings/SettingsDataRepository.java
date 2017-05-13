package ru.android.childdiary.data.repositories.core.settings;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.core.settings.SettingsRepository;

@Singleton
public class SettingsDataRepository implements SettingsRepository {
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_FINISH_TIME = "finish_time";

    private static final LocalTime DEFAULT_START_TIME = new LocalTime(8, 0);
    private static final LocalTime DEFAULT_FINISH_TIME = new LocalTime(22, 0);

    private final RxSharedPreferences preferences;

    @Inject
    public SettingsDataRepository(RxSharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Observable<LocalTime> getStartTime() {
        return preferences.getObject(KEY_START_TIME, DEFAULT_START_TIME,
                new LocalTimeAdapter(DEFAULT_START_TIME))
                .asObservable();
    }

    @Override
    public void setStartTime(@NonNull LocalTime startTime) {
    }

    @Override
    public Observable<LocalTime> getStartTimeOnce() {
        return getStartTime().first(DEFAULT_START_TIME).toObservable();
    }

    @Override
    public Observable<LocalTime> getFinishTime() {
        return preferences.getObject(KEY_FINISH_TIME, DEFAULT_FINISH_TIME,
                new LocalTimeAdapter(DEFAULT_FINISH_TIME))
                .asObservable();
    }

    @Override
    public void setFinishTime(@NonNull LocalTime finishTime) {
    }

    @Override
    public Observable<LocalTime> getFinishTimeOnce() {
        return getFinishTime().first(DEFAULT_FINISH_TIME).toObservable();
    }

    @Override
    public Observable<LocalTime> setStartTimeObservable(@NonNull LocalTime startTime) {
        return null;
    }

    @Override
    public Observable<LocalTime> setFinishTimeObservable(@NonNull LocalTime finishTime) {
        return null;
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
