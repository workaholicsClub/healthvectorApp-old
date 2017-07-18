package ru.android.childdiary.data.repositories.development.testing;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingRepository;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.domain.interactors.development.testing.tests.AutismTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanMentalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanPhysicalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.NewbornTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.utils.strings.TestUtils;

@Singleton
public class TestingDataRepository implements TestingRepository {
    private final Context context;
    private final TestingDbService testingDbService;
    private final List<Test> tests = new ArrayList<>();

    @Inject
    public TestingDataRepository(Context context, TestingDbService testingDbService) {
        this.context = context;
        this.testingDbService = testingDbService;
        initTests();
    }

    private void initTests() {
        for (TestType testType : TestType.values()) {
            Test test = createTest(testType);
            tests.add(test);
        }
    }

    @Nullable
    private Test createTest(@Nullable TestType testType) {
        if (testType == null) {
            return null;
        }
        switch (testType) {
            case DOMAN_PHYSICAL:
                return DomanPhysicalTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(getDomanPhysicalQuestions())
                        .resultTextFormat(context.getString(R.string.doman_result_text_format))
                        .advanced(context.getString(R.string.doman_stage_advanced))
                        .normal(context.getString(R.string.doman_stage_normal))
                        .slow(context.getString(R.string.doman_stage_slow))
                        .stageTitles(getStageTitles())
                        .stageDescriptions(getStrings(R.array.stage_descriptions))
                        .build();
            case DOMAN_MENTAL:
                return DomanMentalTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(getDomanMentalQuestions())
                        .resultTextFormat(context.getString(R.string.doman_result_text_format))
                        .advanced(context.getString(R.string.doman_stage_advanced))
                        .normal(context.getString(R.string.doman_stage_normal))
                        .slow(context.getString(R.string.doman_stage_slow))
                        .stageTitles(getStageTitles())
                        .stageDescriptions(getStrings(R.array.stage_descriptions))
                        .build();
            case AUTISM:
                String finishText = context.getString(R.string.test_autism_finish_text);
                return AutismTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(getQuestions(R.array.test_autism))
                        .finishTextHigh(context.getString(R.string.test_autism_finish_text_high) + finishText)
                        .finishTextMedium(context.getString(R.string.test_autism_finish_text_medium) + finishText)
                        .finishTextLow(context.getString(R.string.test_autism_finish_text_low) + finishText)
                        .build();
            case NEWBORN:
                return NewbornTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(getQuestions(R.array.test_newborn))
                        .resultBad(context.getString(R.string.test_newborn_result_bad))
                        .resultGood(context.getString(R.string.test_newborn_result_good))
                        .build();
        }
        return null;
    }

    private List<String> getStageTitles() {
        return Observable.range(1, 7)
                .map(i -> context.getString(R.string.stage, i))
                .toList()
                .blockingGet();
    }

    private Map<DomanTestParameter, List<Question>> getDomanPhysicalQuestions() {
        Map<DomanTestParameter, List<Question>> map = new HashMap<>();
        List<Question> list;
        list = getQuestions(R.array.physical_manual_stages);
        map.put(DomanTestParameter.PHYSICAL_MANUAL, list);
        list = getQuestions(R.array.physical_mobility_stages);
        map.put(DomanTestParameter.PHYSICAL_MOBILITY, list);
        list = getQuestions(R.array.physical_speech_stages);
        map.put(DomanTestParameter.PHYSICAL_SPEECH, list);
        return map;
    }

    private Map<DomanTestParameter, List<Question>> getDomanMentalQuestions() {
        Map<DomanTestParameter, List<Question>> map = new HashMap<>();
        List<Question> list;
        list = getQuestions(R.array.mental_audition_stages);
        map.put(DomanTestParameter.MENTAL_AUDITION, list);
        list = getQuestions(R.array.mental_sensitivity_stages);
        map.put(DomanTestParameter.MENTAL_SENSITIVITY, list);
        list = getQuestions(R.array.mental_vision_stages);
        map.put(DomanTestParameter.MENTAL_VISION, list);
        return map;
    }

    private List<Question> getQuestions(@ArrayRes int id) {
        return Observable.fromArray(context.getResources().getStringArray(id))
                .map(text -> Question.builder().text(text).build())
                .toList()
                .blockingGet();
    }

    private List<String> getStrings(@ArrayRes int id) {
        return Observable.fromArray(context.getResources().getStringArray(id))
                .toList()
                .blockingGet();
    }

    @Override
    public Observable<Test> getTest(@NonNull TestType testType) {
        return Observable.fromCallable(() -> getTestByType(testType));
    }

    private Test getTestByType(@NonNull TestType testType) {
        for (Test test : tests) {
            if (test.getTestType() == testType) {
                return test;
            }
        }
        throw new IllegalStateException("Unsupported test type");
    }

    @Override
    public Observable<List<Test>> getTests() {
        return Observable.just(tests);
    }

    @Override
    public Observable<List<TestResult>> getTestResults(@NonNull TestResultsRequest request) {
        return testingDbService.getTestResults(request)
                .map(this::putTest);
    }

    private List<TestResult> putTest(@NonNull List<TestResult> testResults) {
        return Observable.fromIterable(testResults)
                .map(testResult -> testResult.toBuilder().test(getTestByType(testResult.getTestType())).build())
                .toList()
                .blockingGet();
    }

    @Override
    public Observable<TestResult> add(@NonNull TestResult testResult) {
        return testingDbService.add(testResult);
    }

    @Override
    public Observable<TestResult> delete(@NonNull TestResult testResult) {
        return testingDbService.delete(testResult);
    }

    public Single<Boolean> checkDate(@NonNull Child child,
                                     @NonNull TestType testType,
                                     @NonNull DomanTestParameter testParameter,
                                     @NonNull LocalDate date) {
        return testingDbService.checkDate(child, testType, testParameter, date);
    }
}
