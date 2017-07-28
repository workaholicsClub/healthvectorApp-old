package ru.android.childdiary.data.repositories.development.testing;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.query.WhereAndOr;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import lombok.val;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.development.testing.TestResultEntity;
import ru.android.childdiary.data.repositories.development.testing.mappers.TestResultMapper;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;

@Singleton
public class TestingDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final TestResultMapper testResultMapper;

    @Inject
    public TestingDbService(ReactiveEntityStore<Persistable> dataStore,
                            TestResultMapper testResultMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.testResultMapper = testResultMapper;
    }

    public Observable<List<TestResult>> getTestResults(@NonNull TestResultsRequest request) {
        WhereAndOr<ReactiveResult<TestResultEntity>> select = dataStore.select(TestResultEntity.class)
                .where(TestResultEntity.CHILD_ID.eq(request.getChild().getId()));
        if (request.getTestType() != null) {
            select = select.and(TestResultEntity.TEST_TYPE.eq(request.getTestType()));
        }
        if (request.getTestParameter() != null) {
            select = select.and(TestResultEntity.DOMAN_TEST_PARAMETER.eq(request.getTestParameter()));
        }
        val order = request.isAscending() ? TestResultEntity.TEST_RESULT_DATE.asc() : TestResultEntity.TEST_RESULT_DATE.desc();
        return select.orderBy(order, TestResultEntity.TEST_TYPE, TestResultEntity.DOMAN_TEST_PARAMETER, TestResultEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, testResultMapper));
    }

    public Observable<TestResult> add(@NonNull TestResult testResult) {
        return DbUtils.insertObservable(blockingEntityStore, testResult, testResultMapper);
    }

    public Observable<TestResult> delete(@NonNull TestResult testResult) {
        return DbUtils.deleteObservable(blockingEntityStore, TestResultEntity.class, testResult, testResult.getId());
    }

    public Single<Boolean> checkDate(@NonNull Child child,
                                     @NonNull TestType testType,
                                     @NonNull DomanTestParameter testParameter,
                                     @NonNull LocalDate date) {
        return dataStore.count(TestResultEntity.class)
                .where(TestResultEntity.CHILD_ID.eq(child.getId()))
                .and(TestResultEntity.TEST_TYPE.eq(testType))
                .and(TestResultEntity.DOMAN_TEST_PARAMETER.eq(testParameter))
                .and(TestResultEntity.TEST_RESULT_DATE.eq(date))
                .get()
                .single()
                .map(count -> count == 0);
    }
}
