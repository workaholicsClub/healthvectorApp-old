package ru.android.childdiary.di.modules;

import android.content.Context;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Arrays;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.childdiary.R;

@Module
public class CloudModule {
    private static final String[] SCOPES = {DriveScopes.DRIVE_APPDATA};

    @Provides
    @Singleton
    public GoogleAccountCredential provideCredential(Context context) {
        return GoogleAccountCredential.usingOAuth2(context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    @Provides
    @Singleton
    public Drive provideDrive(Context context, GoogleAccountCredential credential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Drive.Builder(transport, jsonFactory, credential)
                .setApplicationName(context.getString(R.string.app_name))
                .build();
    }
}
