package ru.android.healthvector.presentation.testing.dialogs;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.TestingInteractor;
import ru.android.healthvector.domain.development.testing.data.processors.core.TestParameters;
import ru.android.healthvector.presentation.core.BasePresenter;

@InjectViewState
public class TestParametersPresenter extends BasePresenter<TestParametersView> {
    @Inject
    TestingInteractor testingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void check(@NonNull Child child, @NonNull TestType testType, @NonNull TestParameters testParameters) {
        if (testParameters.getParameter() == null || testParameters.getDate() == null) {
            logger.error("invalid test parameters");
            return;
        }
        unsubscribeOnDestroy(
                testingInteractor.checkDate(child, testType, testParameters.getParameter(), testParameters.getDate())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isOk -> {
                                    if (isOk) {
                                        getViewState().close(child, testParameters);
                                    } else {
                                        getViewState().dateAlreadyUsed(testParameters.getDate(), testType, testParameters);
                                    }
                                },
                                this::onUnexpectedError));
    }
}
