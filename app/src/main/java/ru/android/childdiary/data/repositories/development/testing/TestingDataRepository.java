package ru.android.childdiary.data.repositories.development.testing;

import android.content.Context;
import android.content.res.Resources;
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
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.core.LocalizationUtils;
import ru.android.childdiary.domain.development.testing.TestingRepository;
import ru.android.childdiary.domain.development.testing.data.TestResult;
import ru.android.childdiary.domain.development.testing.data.tests.AutismTest;
import ru.android.childdiary.domain.development.testing.data.tests.DomanMentalTest;
import ru.android.childdiary.domain.development.testing.data.tests.DomanPhysicalTest;
import ru.android.childdiary.domain.development.testing.data.tests.NewbornTest;
import ru.android.childdiary.domain.development.testing.data.tests.core.Question;
import ru.android.childdiary.domain.development.testing.data.tests.core.Test;
import ru.android.childdiary.domain.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.utils.ui.FormatTextHelper;

@Singleton
public class TestingDataRepository implements TestingRepository {
    private static final String LANGUAGE_EN = "en", LANGUAGE_RU = "ru";

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
        Container<String>
                nameEn = new Container<>(),
                nameRu = new Container<>(),
                descriptionEn = new Container<>(),
                descriptionRu = new Container<>();
        LocalizationUtils.fillFromResources(context, LANGUAGE_EN,
                resources -> {
                    nameEn.put(getTestName(resources, testType));
                    descriptionEn.put(getTestDescription(resources, testType));
                });
        LocalizationUtils.fillFromResources(context, LANGUAGE_RU,
                resources -> {
                    nameRu.put(getTestName(resources, testType));
                    descriptionRu.put(getTestDescription(resources, testType));
                });
        switch (testType) {
            case DOMAN_PHYSICAL:
                return DomanPhysicalTest.builder()
                        .nameEn(nameEn.get())
                        .nameRu(nameRu.get())
                        .descriptionEn(descriptionEn.get())
                        .descriptionRu(descriptionRu.get())
                        .questions(getDomanPhysicalQuestions())
                        .build();
            case DOMAN_MENTAL:
                return DomanMentalTest.builder()
                        .nameEn(nameEn.get())
                        .nameRu(nameRu.get())
                        .descriptionEn(descriptionEn.get())
                        .descriptionRu(descriptionRu.get())
                        .questions(getDomanMentalQuestions())
                        .build();
            case AUTISM:
                return AutismTest.builder()
                        .nameEn(nameEn.get())
                        .nameRu(nameRu.get())
                        .descriptionEn(descriptionEn.get())
                        .descriptionRu(descriptionRu.get())
                        .questions(getAutismQuestions())
                        .build();
            case NEWBORN:
                return NewbornTest.builder()
                        .nameEn(nameEn.get())
                        .nameRu(nameRu.get())
                        .descriptionEn(descriptionEn.get())
                        .descriptionRu(descriptionRu.get())
                        .questions(getNewbornQuestions())
                        .build();
        }
        return null;
    }

    @Nullable
    private String getTestName(Resources resources, @Nullable TestType testType) {
        if (testType == null) {
            return null;
        }
        switch (testType) {
            case DOMAN_MENTAL:
                return resources.getString(R.string.test_doman_mental_name);
            case DOMAN_PHYSICAL:
                return resources.getString(R.string.test_doman_physical_name);
            case AUTISM:
                return resources.getString(R.string.test_autism_name);
            case NEWBORN:
                return resources.getString(R.string.test_newborn_name);
        }
        return null;
    }

    @Nullable
    private String getTestDescription(Resources resources, @Nullable TestType testType) {
        if (testType == null) {
            return null;
        }
        switch (testType) {
            case DOMAN_PHYSICAL:
            case DOMAN_MENTAL:
                return FormatTextHelper.getParagraphsWithJustifyAlignment(resources.getStringArray(R.array.test_doman_description_paragraphs));
            case AUTISM:
                return FormatTextHelper.getParagraphsWithJustifyAlignment(resources.getStringArray(R.array.test_autism_description_paragraphs));
            case NEWBORN:
                return FormatTextHelper.getParagraphWithJustifyAlignment(resources.getString(R.string.test_newborn_description));
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

    private List<Question> getAutismQuestions() {
        return getQuestions(R.array.test_autism, true);
    }

    private List<Question> getNewbornQuestions() {
        return getQuestions(R.array.test_newborn, true);
    }

    private List<Question> getQuestions(@ArrayRes int arrayId, boolean withNumbers) {
        Container<String>
                numberFromCountFormatEn = new Container<>(),
                numberFromCountFormatRu = new Container<>();
        Container<String[]>
                stringsEn = new Container<>(),
                stringsRu = new Container<>();
        LocalizationUtils.fillFromResources(context, LANGUAGE_EN, resources -> {
            stringsEn.put(resources.getStringArray(arrayId));
            numberFromCountFormatEn.put(resources.getString(R.string.question_format));
        });
        LocalizationUtils.fillFromResources(context, LANGUAGE_RU, resources -> {
            stringsRu.put(resources.getStringArray(arrayId));
            numberFromCountFormatRu.put(resources.getString(R.string.question_format));
        });

        List<Question> questions = new ArrayList<>();
        int count = Math.min(stringsEn.get().length, stringsRu.get().length);
        for (int i = 0; i < count; ++i) {
            String textEn = stringsEn.get()[i], textRu = stringsRu.get()[i];
            textEn = formatQuestionText(numberFromCountFormatEn.get(), i, count, textEn, withNumbers);
            textRu = formatQuestionText(numberFromCountFormatRu.get(), i, count, textRu, withNumbers);
            questions.add(Question.builder().textEn(textEn).textRu(textRu).build());
        }
        return questions;
    }

    private String formatQuestionText(String numberFromCountFormat, int i, int count, String text, boolean withNumbers) {
        text = FormatTextHelper.getParagraphWithJustifyAlignment(text);
        if (withNumbers) {
            String numberStr = String.format(numberFromCountFormat, i + 1, count);
            return FormatTextHelper.getParagraphWithLeftAlignment(numberStr)
                    + text;
        }
        return text;
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

    private static class Container<T> {
        T object;

        public void put(T object) {
            this.object = object;
        }

        public T get() {
            return object;
        }
    }
}
