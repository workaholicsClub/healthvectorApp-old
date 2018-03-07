package ru.android.healthvector.presentation.development.partitions.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.TestingInteractor;
import ru.android.healthvector.domain.development.testing.data.TestResult;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;
import ru.android.healthvector.domain.development.testing.requests.TestResultsRequest;
import ru.android.healthvector.presentation.core.AppPartitionPresenter;
import ru.android.healthvector.presentation.core.adapters.DeletedItemsManager;

@InjectViewState
public class TestResultsPresenter extends AppPartitionPresenter<TestResultsView> {
    @Inject
    TestingInteractor testingInteractor;
    private DeletedItemsManager<TestResult> deletedItemsManager = new DeletedItemsManager<>();
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

    public void reviewTestResult(@NonNull TestResult testResult) {
        if (deletedItemsManager.check(testResult)) {
            return;
        }
        getViewState().navigateToTestResult(testResult);
    }

    public void deleteTestResult(@NonNull TestResult testResult) {
        if (deletedItemsManager.check(testResult)) {
            return;
        }
        getViewState().confirmDeletion(testResult);
    }

    public void deletionConfirmed(@NonNull TestResult testResult) {
        if (deletedItemsManager.checkAndAdd(testResult)) {
            return;
        }
        unsubscribeOnDestroy(
                testingInteractor.delete(testResult)
                        .doOnNext(deletedTestResult -> logger.debug("deleted: " + deletedTestResult))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }

    public void showChart() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMapSingle(testingInteractor::hasChartData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    if (response.isHasData()) {
                                        getViewState().navigateToChart(response.getChild());
                                    } else {
                                        getViewState().noChartData();
                                    }
                                },
                                this::onUnexpectedError));
    }

    public void addProfile() {
        getViewState().navigateToProfileAdd();
    }
}
