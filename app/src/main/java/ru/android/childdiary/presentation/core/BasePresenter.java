package ru.android.childdiary.presentation.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class BasePresenter<V extends BaseView> extends MvpPresenter<V> implements ErrorHandler {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Getter(AccessLevel.PROTECTED)
    @Inject
    Context context;

    protected void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        // TODO: report to crashlytics
        getViewState().onUnexpectedError(e);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
