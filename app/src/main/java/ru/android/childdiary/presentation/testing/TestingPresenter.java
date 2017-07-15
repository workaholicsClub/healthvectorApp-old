package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.BiTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestFactory;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestParameters;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingQuestionArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingStartArguments;

@InjectViewState
public class TestingPresenter extends BasePresenter<TestingView> implements TestingController {
    @Inject
    TestingInteractor testingInteractor;

    private Test test;
    private Child child;
    private LocalDate date;
    private BiTestProcessor testProcessor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void initTest(@NonNull Test test,
                         @NonNull Child child,
                         @NonNull LocalDate date) {
        this.test = test;
        this.child = child;
        this.date = date;
        getViewState().showStart(TestingStartArguments.testingStartBuilder()
                .child(child)
                .selectedDate(date)
                .test(test)
                .build());
    }

    @Override
    public void startTesting() {
        if (test.getTestType() == TestType.DOMAN_MENTAL || test.getTestType() == TestType.DOMAN_PHYSICAL) {
            getViewState().specifyTestParameters(child, test);
        } else {
            testProcessor = TestFactory.createTestProcessor(test, TestParameters.builder().build());
            getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                    .child(child)
                    .selectedDate(date)
                    .question(testProcessor.getCurrentQuestion())
                    .forward(true)
                    .parameter(getDomanParameter())
                    .build());
        }
    }

    public void onTestParametersSet(@NonNull TestParameters parameters) {
        // TODO check date
        testProcessor = TestFactory.createTestProcessor(test, parameters);
        getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                .child(child)
                .selectedDate(date)
                .question(testProcessor.getCurrentQuestion())
                .forward(true)
                .parameter(getDomanParameter())
                .build());
    }

    @Override
    public void stopTesting() {
        if (testProcessor == null || testProcessor.isFinished()) {
            close();
        } else {
            getViewState().showCloseConfirmation();
        }
    }

    public void close() {
        if (testProcessor != null) {
            if (testProcessor.isFinished()) {
                unsubscribeOnDestroy(testingInteractor.add(TestResult.builder()
                        .child(child)
                        .testType(test.getTestType())
                        .date(date)
                        .domanTestParameter(getDomanParameter())
                        .result(testProcessor.getResultNumber())
                        .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(added -> getViewState().close(), this::onUnexpectedError));
            }
            testProcessor = null;
        }
        getViewState().close();
    }

    @Override
    public void answerYes() {
        testProcessor.answer(true);
        goToNextQuestion();
    }

    @Override
    public void answerNo() {
        testProcessor.answer(false);
        goToNextQuestion();
    }

    private void goToNextQuestion() {
        Boolean forward = testProcessor.goToNextQuestion();
        if (testProcessor.isFinished()) {
            String text = testProcessor.getResultText();
            getViewState().showFinish(TestingFinishArguments.testingFinishBuilder()
                    .child(child)
                    .selectedDate(date)
                    .text(text)
                    .parameter(getDomanParameter())
                    .result(getDomanResult())
                    .build());
        } else {
            getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                    .child(child)
                    .selectedDate(date)
                    .question(testProcessor.getCurrentQuestion())
                    .forward(forward)
                    .parameter(getDomanParameter())
                    .build());
        }
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
}
