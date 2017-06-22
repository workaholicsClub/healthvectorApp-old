package ru.android.childdiary.data.availability;

import android.content.Context;

import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

import io.reactivex.Single;

public class PlayServicesAvailability {
    private final Context context;

    @Inject
    public PlayServicesAvailability(Context context) {
        this.context = context;
    }

    private int getPlayServicesAvailabilityResult() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        return apiAvailability.isGooglePlayServicesAvailable(context);
    }

    public Single<Integer> checkPlayServicesAvailability() {
        return Single.fromCallable(this::getPlayServicesAvailabilityResult);
    }
}
