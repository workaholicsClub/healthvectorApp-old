package ru.android.healthvector.di.modules;

import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import ru.android.healthvector.BuildConfig;
import ru.android.healthvector.data.network.interceptors.FakeInterceptor;
import ru.android.healthvector.data.repositories.exercises.ExerciseNetworkService;

@Module
public class NetworkModule {
    private static final String API_BASE_URL = "https://healthvector.ru/";
    private static final long TIMEOUT_IN_SECONDS = 15;

    @SuppressWarnings("ConstantConditions")
    @Nullable
    private static Interceptor getLoggingInterceptor() {
        if (BuildConfig.PRINT_HTTP_LOGS) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return interceptor;
        }
        return null;
    }

    @Nullable
    private static Interceptor getFakeInterceptor() {
        if (BuildConfig.USE_FAKE_INTERCEPTOR) {
            return new FakeInterceptor();
        }
        return null;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Interceptor loggingInterceptor = getLoggingInterceptor();
        if (loggingInterceptor != null) {
            builder.addInterceptor(loggingInterceptor);
        }
        Interceptor fakeInterceptor = getFakeInterceptor();
        if (fakeInterceptor != null) {
            builder.addInterceptor(fakeInterceptor);
        }
        builder.connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public ExerciseNetworkService provideExerciseNetworkService(Retrofit retrofit) {
        return retrofit.create(ExerciseNetworkService.class);
    }
}
