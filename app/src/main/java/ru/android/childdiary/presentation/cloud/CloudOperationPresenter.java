package ru.android.childdiary.presentation.cloud;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.cloud.core.CloudPresenter;

@InjectViewState
public class CloudOperationPresenter extends CloudPresenter<CloudOperationView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(cloudInteractor.getAccountName()
                .map(TextUtils::isEmpty)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(firstTime -> {
                    if (firstTime) {
                        getViewState().showState(CloudOperationState.NOT_AUTHORIZED);
                    } else {
                        getViewState().showState(CloudOperationState.AUTHORIZED);
                    }
                }, this::onUnexpectedError));
    }

    @Override
    public void moveNext() {
        getViewState().navigateToMain();
    }
}
