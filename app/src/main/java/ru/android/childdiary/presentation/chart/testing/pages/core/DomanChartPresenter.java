package ru.android.childdiary.presentation.chart.testing.pages.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.data.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.core.DomanResult;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.data.TestFactory;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.presentation.chart.core.ChartPresenter;
import ru.android.childdiary.presentation.chart.testing.dialogs.ParameterDialogArguments;
import ru.android.childdiary.utils.strings.TestUtils;

@InjectViewState
public class DomanChartPresenter extends ChartPresenter<DomanChartView> {
    @Inject
    TestingInteractor testingInteractor;

    @Inject
    TestFactory testFactory;

    private Child child;
    private TestType testType;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadResults();
    }

    public void init(@NonNull Child child, @NonNull TestType testType) {
        this.child = child;
        this.testType = testType;
    }

    public void showFilter() {
        unsubscribeOnDestroy(
                Observable.combineLatest(
                        testingInteractor.getTest(testType),
                        testingInteractor.getSelectedParameterOnce(testType),
                        (test, testParameter) -> ParameterDialogArguments.builder()
                                .test(test)
                                .selectedParameter(testParameter)
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::specifyParameter, this::onUnexpectedError));
    }

    public void selectTestParameter(@NonNull DomanTestParameter testParameter) {
        unsubscribeOnDestroy(testingInteractor.setSelectedParameter(testType, testParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(parameter -> logger.debug("select " + parameter), this::onUnexpectedError));
    }

    private void loadResults() {
        unsubscribeOnDestroy(
                testingInteractor.getSelectedParameter(testType)
                        .flatMapSingle(
                                testParameter -> testingInteractor.getTestResults(TestResultsRequest.builder()
                                        .child(child)
                                        .testType(testType)
                                        .testParameter(testParameter)
                                        .ascending(true)
                                        .build())
                                        .first(Collections.emptyList())
                                        .map(testResults -> DomanChartState.builder()
                                                .testType(testType)
                                                .testParameter(testParameter)
                                                .testResults(map(testResults))
                                                .invalidResults(TestUtils.invalidResults(testResults))
                                                .build()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showResults, this::onUnexpectedError));
    }

    private List<DomanResult> map(@NonNull List<TestResult> results) {
        return Observable.fromIterable(results)
                .map(this::map)
                .toList()
                .blockingGet();
    }

    private DomanResult map(@NonNull TestResult testResult) {
        DomanTestProcessor testProcessor = (DomanTestProcessor) testFactory.createTestProcessor(testResult);
        return testProcessor.getDomanResult();
    }
}
