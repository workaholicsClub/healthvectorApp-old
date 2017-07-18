package ru.android.childdiary.presentation.testing.dynamic;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.val;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestFactory;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class DomanChartPresenter extends BasePresenter<DomanChartView> {
    @Inject
    TestingInteractor testingInteractor;

    private Child child;
    private TestType testType;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadResults();
    }

    public void init(@NonNull Child child, @NonNull TestType testType) {
        this.child = child;
        this.testType = testType;
    }

    private void loadResults() {
        unsubscribeOnDestroy(
                testingInteractor.getDomanTestResults(child, testType)
                        .map(this::map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showResults, this::onUnexpectedError));
    }

    private LinkedHashMap<DomanTestParameter, List<DomanResult>> map(@NonNull LinkedHashMap<DomanTestParameter, List<TestResult>> map) {
        LinkedHashMap<DomanTestParameter, List<DomanResult>> result = new LinkedHashMap<>();
        for (val entry : map.entrySet()) {
            result.put(entry.getKey(), map(entry.getValue()));
        }
        return result;
    }

    private List<DomanResult> map(@NonNull List<TestResult> results) {
        return Observable.fromIterable(results)
                .map(this::map)
                .toList()
                .blockingGet();
    }

    private DomanResult map(@NonNull TestResult testResult) {
        DomanTestProcessor testProcessor = (DomanTestProcessor) TestFactory.createTestProcessor(testResult);
        return testProcessor.getDomanResult();
    }
}
