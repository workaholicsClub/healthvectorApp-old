package ru.android.childdiary.presentation.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.utils.log.LogSystem;

public abstract class BasePresenter<V extends BaseView> extends MvpPresenter<V> implements ErrorHandler {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
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
