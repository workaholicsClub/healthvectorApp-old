package ru.android.childdiary.data.availability;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import io.reactivex.Single;

public class NetworkAvailability {
    private final Context context;

    @Inject
    public NetworkAvailability(Context context) {
        this.context = context;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public Single<Boolean> isNetworkAvailable() {
        return Single.fromCallable(this::isNetworkConnected);
    }
}
