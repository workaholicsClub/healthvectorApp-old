package ru.android.healthvector.data.availability;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.android.healthvector.data.availability.exceptions.NetworkUnavailableException;

public class NetworkAvailability {
    private final Context context;

    @Inject
    public NetworkAvailability(Context context) {
        this.context = context;
    }

    private boolean getNetworkAvailabilityResult() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public Single<Boolean> checkNetworkAvailability(boolean failFast) {
        return Single.fromCallable(() -> {
            boolean result = getNetworkAvailabilityResult();
            if (!result && failFast) {
                throw new NetworkUnavailableException("No network connection on the device");
            }
            return result;
        });
    }
}
