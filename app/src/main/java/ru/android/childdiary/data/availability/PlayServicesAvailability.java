package ru.android.childdiary.data.availability;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

import io.reactivex.Single;

public class PlayServicesAvailability {
    private final Context context;

    @Inject
    public PlayServicesAvailability(Context context) {
        this.context = context;
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    public Single<Boolean> isPlayServicesAvailable() {
        return Single.fromCallable(this::isGooglePlayServicesAvailable);
    }
}
