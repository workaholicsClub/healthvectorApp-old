package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.testing.TestingInteractor;
import ru.android.childdiary.domain.development.testing.data.TestResult;
import ru.android.childdiary.domain.development.testing.data.interpreters.core.TestInterpreter;
import ru.android.childdiary.domain.development.testing.data.processors.core.BaseTestProcessor;
import ru.android.childdiary.domain.development.testing.data.processors.core.DomanResult;
import ru.android.childdiary.domain.development.testing.data.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.development.testing.data.TestFactory;
import ru.android.childdiary.domain.development.testing.data.tests.core.Test;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishArguments;

@InjectViewState
public class TestResultPresenter extends BasePresenter<TestResultView> {
    @Inject
    TestingInteractor testingInteractor;

    @Inject
    TestFactory testFactory;

    private TestResult testResult;
    private BaseTestProcessor testProcessor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void initTestResult(@NonNull TestResult testResult) {
        this.testResult = testResult;
        testProcessor = testFactory.createTestProcessor(testResult);
        showFinishPage(testResult.getTest(), testResult.getChild(), testResult.getDate());
    }

    private void showFinishPage(@NonNull Test test, @NonNull Child child, @NonNull LocalDate date) {
        TestInterpreter testInterpreter = testFactory.createTestInterpreter(testProcessor);
        String text = testInterpreter.interpret();
        getViewState().showFinish(TestingFinishArguments.testingFinishBuilder()
                .child(child)
                .selectedDate(date)
                .test(test)
                .parameter(getDomanParameter())
                .text(text)
                .result(getDomanResult())
                .invalidResults(testResult.isInvalid())
                .isInTestMode(false)
                .build());
    }

    @Nullable
    private DomanTestParameter getDomanParameter() {
        if (testProcessor instanceof DomanTestProcessor) {
            return ((DomanTestProcessor) testProcessor).getDomanParameter();
        }
        return null;
    }

    @Nullable
    private DomanResult getDomanResult() {
        if (testProcessor instanceof DomanTestProcessor) {
            return ((DomanTestProcessor) testProcessor).getDomanResult();
        }
        return null;
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
}
