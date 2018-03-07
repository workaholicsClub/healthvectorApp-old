package ru.android.healthvector.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.android.healthvector.data.availability.NetworkAvailability;
import ru.android.healthvector.data.availability.PlayServicesAvailability;

@Module
public class AvailabilityModule {
    @Provides
    @Singleton
    public NetworkAvailability provideNetworkAvailability(Context context) {
        return new NetworkAvailability(context);
    }

    @Provides
    @Singleton
    public PlayServicesAvailability providePlayServicesAvailability(Context context) {
        return new PlayServicesAvailability(context);
    }
}
