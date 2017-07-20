package ru.android.childdiary.presentation.testing.dynamic;

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
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestFactory;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.testing.dialogs.ParameterDialogArguments;

@InjectViewState
public class DomanChartPresenter extends BasePresenter<DomanChartView> {
    @Inject
    TestingInteractor testingInteractor;

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
                                        .map(this::map)
                                        .map(testResults -> DomanChartState.builder()
                                                .testType(testType)
                                                .testParameter(testParameter)
                                                .testResults(testResults)
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
        DomanTestProcessor testProcessor = (DomanTestProcessor) TestFactory.createTestProcessor(testResult);
        return testProcessor.getDomanResult();
    }
}