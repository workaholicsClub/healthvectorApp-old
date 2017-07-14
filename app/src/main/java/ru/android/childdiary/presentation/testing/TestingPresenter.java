package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.BiTestProcessor;
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
                    .parameter(getParameter())
                    .build());
        }
    }

    public void onTestParametersSet(@NonNull TestParameters parameters) {
        testProcessor = TestFactory.createTestProcessor(test, parameters);
        getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                .child(child)
                .selectedDate(date)
                .question(testProcessor.getCurrentQuestion())
                .forward(true)
                .parameter(getParameter())
                .build());
    }

    @Nullable
    private DomanTestParameter getParameter() {
        if (testProcessor instanceof DomanTestProcessor) {
            return ((DomanTestProcessor) testProcessor).getParameter();
        }
        return null;
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
                // TODO: save test result
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
                    .parameter(getParameter())
                    .build());
        } else {
            getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                    .child(child)
                    .selectedDate(date)
                    .question(testProcessor.getCurrentQuestion())
                    .forward(forward)
                    .parameter(getParameter())
                    .build());
        }
    }
}
