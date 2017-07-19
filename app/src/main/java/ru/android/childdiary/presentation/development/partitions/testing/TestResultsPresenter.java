package ru.android.childdiary.presentation.development.partitions.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class TestResultsPresenter extends AppPartitionPresenter<TestResultsView> {
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
                testingInteractor.getTestResults(TestResultsRequest.builder()
                        .child(child)
                        .testType(null)
                        .testParameter(null)
                        .build())
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
                                child -> getViewState().navigateToTest(test, child, LocalDate.now()),
                                this::onUnexpectedError));
    }

    public void deleteTestResult(@NonNull TestResult testResult) {
        getViewState().confirmDeletion(testResult);
    }

    public void deletionConfirmed(@NonNull TestResult testResult) {
        unsubscribeOnDestroy(
                testingInteractor.delete(testResult)
                        .doOnNext(deletedTestResult -> logger.debug("deleted: " + deletedTestResult))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }

    public void reviewTestResult(@NonNull TestResult testResult) {
        getViewState().navigateToTestResult(testResult);
    }

    public void showChart() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMapSingle(child -> testingInteractor.hasDomanTestResults(child))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.isHasData()) {
                                        getViewState().navigateToChart(response.getChild());
                                    } else {
                                        getViewState().noChartData();
                                    }
                                },
                                this::onUnexpectedError));
    }
}
