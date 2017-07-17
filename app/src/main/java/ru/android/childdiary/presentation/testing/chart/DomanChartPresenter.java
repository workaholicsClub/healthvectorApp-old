package ru.android.childdiary.presentation.testing.chart;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestFactory;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestParameters;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.BasePresenter;

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

    private void loadResults() {
        unsubscribeOnDestroy(
                testingInteractor.getTest(testType)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::testLoaded, this::onUnexpectedError));
    }

    private void testLoaded(@NonNull Test test) {
        unsubscribeOnDestroy(
                testingInteractor.getTestResults(TestResultsRequest.builder()
                        .child(child)
                        .testType(test.getTestType())
                        .testParameter(null)
                        .build()) // TODO select only doman and by parameter
                        .map(results -> map(test, results))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showResults, this::onUnexpectedError));
    }

    private List<DomanResult> map(@NonNull Test test, @NonNull List<TestResult> results) {
        return Observable.fromIterable(results)
                .filter(result -> result.getTestType() == test.getTestType()
                        && result.getDomanTestParameter() != null)
                .map(result -> map(test, result))
                .toList()
                .blockingGet();
    }

    private DomanResult map(@NonNull Test test, @NonNull TestResult testResult) {
        DomanTestProcessor testProcessor = (DomanTestProcessor) TestFactory.createTestProcessor(test, TestParameters.builder()
                .birthDate(testResult.getBirthDate())
                .date(testResult.getDate())
                .parameter(testResult.getDomanTestParameter())
                .build());
        testProcessor.setResult(testResult.getResult());
        return testProcessor.getDomanResult();
    }
}
