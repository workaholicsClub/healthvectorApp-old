package ru.android.childdiary.data.repositories.development.testing;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

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
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestingRepository;
import ru.android.childdiary.domain.interactors.development.testing.data.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.AutismTest;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.DomanMentalTest;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.DomanPhysicalTest;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.NewbornTest;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.Question;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.Test;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.utils.strings.TestUtils;

@Singleton
public class TestingDataRepository implements TestingRepository {
    private static final String KEY_PHYSICAL_PARAMETER = "physical_parameter";
    private static final String KEY_MENTAL_PARAMETER = "mental_parameter";

    private final Context context;
    private final RxSharedPreferences preferences;
    private final TestingDbService testingDbService;
    private final List<Test> tests = new ArrayList<>();

    @Inject
    public TestingDataRepository(Context context,
                                 RxSharedPreferences preferences,
                                 TestingDbService testingDbService) {
        this.context = context;
        this.preferences = preferences;
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
                        .build();
            case DOMAN_MENTAL:
                return DomanMentalTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(getDomanMentalQuestions())
                        .build();
            case AUTISM:
                return AutismTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(getQuestions(R.array.test_autism, true))
                        .build();
            case NEWBORN:
                return NewbornTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(getQuestions(R.array.test_newborn, true))
                        .build();
        }
        return null;
    }

    private Map<DomanTestParameter, List<Question>> getDomanPhysicalQuestions() {
        Map<DomanTestParameter, List<Question>> map = new HashMap<>();
        List<Question> list;
        list = getQuestions(R.array.physical_manual_stages, false);
        map.put(DomanTestParameter.PHYSICAL_MANUAL, list);
        list = getQuestions(R.array.physical_mobility_stages, false);
        map.put(DomanTestParameter.PHYSICAL_MOBILITY, list);
        list = getQuestions(R.array.physical_speech_stages, false);
        map.put(DomanTestParameter.PHYSICAL_SPEECH, list);
        return map;
    }

    private Map<DomanTestParameter, List<Question>> getDomanMentalQuestions() {
        Map<DomanTestParameter, List<Question>> map = new HashMap<>();
        List<Question> list;
        list = getQuestions(R.array.mental_audition_stages, false);
        map.put(DomanTestParameter.MENTAL_AUDITION, list);
        list = getQuestions(R.array.mental_sensitivity_stages, false);
        map.put(DomanTestParameter.MENTAL_SENSITIVITY, list);
        list = getQuestions(R.array.mental_vision_stages, false);
        map.put(DomanTestParameter.MENTAL_VISION, list);
        return map;
    }

    private List<Question> getQuestions(@ArrayRes int id, boolean withNumbers) {
        String[] strings = context.getResources().getStringArray(id);
        int count = strings.length;
        return Observable.zip(
                Observable.range(0, count),
                Observable.fromArray(strings),
                (i, text) -> formatQuestionText(i, count, text, withNumbers))
                .map(text -> Question.builder().text(text).build())
                .toList()
                .blockingGet();
    }

    private String formatQuestionText(int i, int count, String text, boolean withNumbers) {
        return withNumbers ? context.getString(R.string.question_format, i + 1, count, text) : text;
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

    @Override
    public Single<Boolean> checkDate(@NonNull Child child,
                                     @NonNull TestType testType,
                                     @NonNull DomanTestParameter testParameter,
                                     @NonNull LocalDate date) {
        return testingDbService.checkDate(child, testType, testParameter, date);
    }

    @Override
    public Observable<DomanTestParameter> getSelectedParameter(@NonNull TestType testType) {
        switch (testType) {
            case DOMAN_PHYSICAL:
                return preferences.getEnum(KEY_PHYSICAL_PARAMETER,
                        getDefaultParameter(testType),
                        DomanTestParameter.class)
                        .asObservable();
            case DOMAN_MENTAL:
                return preferences.getEnum(KEY_MENTAL_PARAMETER,
                        getDefaultParameter(testType),
                        DomanTestParameter.class)
                        .asObservable();
        }
        throw new IllegalStateException("Unsupported test type");
    }

    @Override
    public Observable<DomanTestParameter> getSelectedParameterOnce(@NonNull TestType testType) {
        return getSelectedParameter(testType)
                .first(getDefaultParameter(testType))
                .toObservable();
    }

    private DomanTestParameter getDefaultParameter(@NonNull TestType testType) {
        switch (testType) {
            case DOMAN_PHYSICAL:
                return DomanTestParameter.PHYSICAL_MOBILITY;
            case DOMAN_MENTAL:
                return DomanTestParameter.MENTAL_VISION;
        }
        throw new IllegalStateException("Unsupported test type");
    }

    @Override
    public Observable<DomanTestParameter> setSelectedParameter(@NonNull TestType testType,
                                                               @NonNull DomanTestParameter testParameter) {
        return Observable.fromCallable(() -> {
            switch (testType) {
                case DOMAN_PHYSICAL:
                    preferences.getEnum(KEY_PHYSICAL_PARAMETER, DomanTestParameter.class)
                            .set(testParameter);
                    return testParameter;
                case DOMAN_MENTAL:
                    preferences.getEnum(KEY_MENTAL_PARAMETER, DomanTestParameter.class)
                            .set(testParameter);
                    return testParameter;
                default:
                    throw new IllegalStateException("Unsupported test type");
            }
        });
    }
}
