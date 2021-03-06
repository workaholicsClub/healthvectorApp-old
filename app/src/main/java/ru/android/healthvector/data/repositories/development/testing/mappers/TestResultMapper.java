package ru.android.healthvector.data.repositories.development.testing.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.db.entities.child.ChildEntity;
import ru.android.healthvector.data.db.entities.development.TestResultData;
import ru.android.healthvector.data.db.entities.development.TestResultEntity;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.testing.data.TestResult;

public class TestResultMapper implements EntityMapper<TestResultData, TestResultEntity, TestResult> {
    private final ChildMapper childMapper;

    @Inject
    public TestResultMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public TestResult mapToPlainObject(@NonNull TestResultData testResultData) {
        ChildData childData = testResultData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        return TestResult.builder()
                .id(testResultData.getId())
                .child(child)
                .testType(testResultData.getTestType())
                .birthDate(testResultData.getBirthDate())
                .date(testResultData.getTestResultDate())
                .domanTestParameter(testResultData.getDomanTestParameter())
                .result(testResultData.getResultNumber())
                .domanDate(testResultData.getDomanDate())
                .build();
    }

    @Override
    public TestResultEntity mapToEntity(BlockingEntityStore blockingEntityStore, @NonNull TestResult testResult) {
        TestResultEntity testResultEntity;
        if (testResult.getId() == null) {
            testResultEntity = new TestResultEntity();
        } else {
            testResultEntity = (TestResultEntity) blockingEntityStore.findByKey(TestResultEntity.class, testResult.getId());
        }
        fillNonReferencedFields(testResultEntity, testResult);
        Child child = testResult.getChild();
        if (child != null) {
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            testResultEntity.setChild(childEntity);
        }
        return testResultEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull TestResultEntity to, @NonNull TestResult from) {
        to.setTestType(from.getTestType());
        to.setBirthDate(from.getBirthDate());
        to.setTestResultDate(from.getDate());
        to.setDomanTestParameter(from.getDomanTestParameter());
        to.setResultNumber(from.getResult());
        to.setDomanDate(from.getDomanDate());
    }
}
