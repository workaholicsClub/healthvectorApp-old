package ru.android.healthvector.presentation.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.android.healthvector.app.ChildDiaryApplication;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.utils.log.LogSystem;

public abstract class BasePresenter<V extends BaseView> extends MvpPresenter<V> {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BasePresenter() {
        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        injectPresenter(component);
    }

    protected abstract void injectPresenter(ApplicationComponent applicationComponent);

    protected final void unsubscribe(@Nullable Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    protected final Disposable unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
        return disposable;
    }

    public void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
        getViewState().onUnexpectedError(e);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        logger.debug("onFirstViewAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("onDestroy");
        compositeDisposable.dispose();
    }
}
