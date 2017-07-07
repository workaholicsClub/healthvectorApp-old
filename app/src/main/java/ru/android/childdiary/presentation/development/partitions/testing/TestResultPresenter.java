package ru.android.childdiary.presentation.development.partitions.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.Test;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class TestResultPresenter extends AppPartitionPresenter<TestResultView> {
    @Inject
    TestingInteractor testingInteractor;

    private Disposable testResultsSubscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(
                testingInteractor.getTests()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showTests, this::onUnexpectedError));

        updateData();
    }

    @Override
    protected void onChildLoaded(@NonNull Child child) {
        super.onChildLoaded(child);
        updateData(child);
    }

    public void updateData() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateData, this::onUnexpectedError));
    }

    private void updateData(@NonNull Child child) {
        unsubscribe(testResultsSubscription);
        testResultsSubscription = unsubscribeOnDestroy(
                testingInteractor.getTestResults(child)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showTestResults, this::onUnexpectedError));
    }

    public void showTestDetails(@NonNull Test test) {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                child -> getViewState().navigateToTest(test.getTestType(), child),
                                this::onUnexpectedError));
    }
}
