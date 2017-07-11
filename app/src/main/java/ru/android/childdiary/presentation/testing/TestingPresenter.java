package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestFactory;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestProcessor;
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
    private TestProcessor testProcessor;

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

    private void checkParameters(boolean checkTestProcessor) {
        if (test == null) {
            throw new IllegalStateException("Testing mode not initialized: test is null");
        }
        if (child == null) {
            throw new IllegalStateException("Testing mode not initialized: child is null");
        }
        if (date == null) {
            throw new IllegalStateException("Testing mode not initialized: date is null");
        }
        if (checkTestProcessor && testProcessor == null) {
            throw new IllegalStateException("Testing mode not initialized: test processor is null");
        }
    }

    @Override
    public void startTesting() {
        checkParameters(false);
        testProcessor = TestFactory.createTestProcessor(test);
        getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                .child(child)
                .selectedDate(date)
                .test(test)
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
                // TODO: save test result
            }
            testProcessor = null;
        }
        getViewState().close();
    }

    @Override
    public void answerYes() {
        checkParameters(true);
        // TODO: answer
        goToNextQuestion();
    }

    @Override
    public void answerNo() {
        checkParameters(true);
        // TODO: answer
        goToNextQuestion();
    }

    private void goToNextQuestion() {
        if (testProcessor.isFinished()) {
            // TODO: get result
            getViewState().showFinish(TestingFinishArguments.testingFinishBuilder()
                    .child(child)
                    .selectedDate(date)
                    .test(test)
                    .build());
        } else {
            testProcessor.goToNextQuestion();
            // TODO: show question
            getViewState().showFinish(TestingFinishArguments.testingFinishBuilder()
                    .child(child)
                    .selectedDate(date)
                    .test(test)
                    .build());
        }
    }
}
