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
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.BiTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestFactory;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestParameters;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.testing.fragments.TestingFinishArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingQuestionArguments;
import ru.android.childdiary.presentation.testing.fragments.TestingStartArguments;
import ru.android.childdiary.utils.strings.TimeUtils;

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
        startTesting(true);
    }

    private void startTesting(boolean checkProfile) {
        if (checkProfile && child.getId() == null) {
            getViewState().noChildSpecified();
            return;
        }
        if (test instanceof DomanTest) {
            getViewState().specifyTestParameters(child, test);
        } else {
            testProcessor = TestFactory.createTestProcessor(test, TestParameters.builder().build());
            getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                    .child(child)
                    .selectedDate(date)
                    .test(test)
                    .parameter(getDomanParameter())
                    .question(testProcessor.getCurrentQuestion())
                    .forward(true)
                    .build());
        }
    }

    public void addProfile() {
        getViewState().navigateToProfileAdd();
    }

    public void continueWithoutProfile() {
        startTesting(false);
    }

    public void onTestParametersSet(@NonNull TestParameters parameters) {
        date = parameters.getDate();
        testProcessor = TestFactory.createTestProcessor(test, parameters);
        getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                .child(child)
                .selectedDate(date)
                .test(test)
                .parameter(getDomanParameter())
                .question(testProcessor.getCurrentQuestion())
                .forward(true)
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
                if (child.getId() == null) {
                    getViewState().close();
                } else {
                    unsubscribeOnDestroy(testingInteractor.add(TestResult.builder()
                            .child(child)
                            .testType(test.getTestType())
                            .birthDate(child.getBirthDate())
                            .date(date)
                            .domanTestParameter(getDomanParameter())
                            .result(testProcessor.getResult())
                            .domanDate(getDomanDate())
                            .build())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(addedTestResult -> getViewState().close(), this::onUnexpectedError));
                }
            }
            testProcessor = null;
        }
        getViewState().close();
    }

    @Override
    public void answerYes() {
        if (testProcessor instanceof DomanTestProcessor) {
            DomanTestProcessor domanTestProcessor = ((DomanTestProcessor) testProcessor);
            LocalDate birthDate = domanTestProcessor.getBirthDate();
            LocalDate date = domanTestProcessor.getDate();
            TimeUtils.Age age = TimeUtils.getAge(birthDate, date);
            if (age != null && age.getMonths() > 1) {
                getViewState().askWhenThisHappened(age);
            } else {
                LocalDate domanDate = domanTestProcessor.getDate();
                domanTestProcessor.setDomanDate(domanDate);
                answerYesAndNext();
            }
        } else {
            answerYesAndNext();
        }
    }

    public void specifyAge(@NonNull TimeUtils.Age age) {
        DomanTestProcessor domanTestProcessor = ((DomanTestProcessor) testProcessor);
        LocalDate domanDate = domanTestProcessor.getBirthDate().plusMonths(age.getMonths());
        domanTestProcessor.setDomanDate(domanDate);
        answerYesAndNext();
    }

    private void answerYesAndNext() {
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
            String text = testProcessor.interpretResult();
            getViewState().showFinish(TestingFinishArguments.testingFinishBuilder()
                    .child(child)
                    .selectedDate(date)
                    .test(test)
                    .parameter(getDomanParameter())
                    .text(text)
                    .result(getDomanResult())
                    .build());
        } else {
            getViewState().showQuestion(TestingQuestionArguments.testingQuestionBuilder()
                    .child(child)
                    .selectedDate(date)
                    .test(test)
                    .parameter(getDomanParameter())
                    .question(testProcessor.getCurrentQuestion())
                    .forward(forward)
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

    @Nullable
    private LocalDate getDomanDate() {
        if (testProcessor instanceof DomanTestProcessor) {
            return ((DomanTestProcessor) testProcessor).getDomanDate();
        }
        return null;
    }
}
