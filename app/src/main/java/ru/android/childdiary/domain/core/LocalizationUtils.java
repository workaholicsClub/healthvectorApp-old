package ru.android.childdiary.domain.core;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Locale;

public class LocalizationUtils {
    public static String getLocalizedName(@Nullable String stringUser, String stringEn, String stringRu) {
        if (stringUser != null) {
            return stringUser;
        }
        boolean isRu = Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage());
        if (isRu) {
            return stringRu;
        }
        return stringEn;
    }

    public static String getLocalizedName(String stringEn, String stringRu) {
        boolean isRu = Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage());
        if (isRu) {
            if (TextUtils.isEmpty(stringRu)) {
                return stringEn;
            }
            return stringRu;
        }
        if (TextUtils.isEmpty(stringEn)) {
            return stringRu;
        }
        return stringEn;
    }

    public static void fillFromResources(Context context, String language, Callback callback) {
        Locale locale = new Locale(language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            configuration.setLocale(locale);
            Resources resources = context.createConfigurationContext(configuration).getResources();

            callback.resourcesReady(resources);
        } else {
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            //noinspection deprecation
            Locale savedLocale = configuration.locale;
            //noinspection deprecation
            configuration.locale = locale;
            //noinspection deprecation
            resources.updateConfiguration(configuration, null);

            callback.resourcesReady(resources);

            //noinspection deprecation
            configuration.locale = savedLocale;
            //noinspection deprecation
            resources.updateConfiguration(configuration, null);
        }
    }

    public interface Callback {
        void resourcesReady(Resources resources);
    }
}
