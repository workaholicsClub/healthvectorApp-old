package ru.android.childdiary.presentation.chart.antropometry.pages.core;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryInteractor;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;
import ru.android.childdiary.domain.interactors.development.antropometry.requests.WhoNormRequest;
import ru.android.childdiary.presentation.chart.antropometry.core.AntropometryChartDataMapper;
import ru.android.childdiary.presentation.chart.core.ChartPresenter;

public abstract class AntropometryChartPresenter extends ChartPresenter<AntropometryChartView> {
    @Inject
    protected AntropometryInteractor antropometryInteractor;

    @Inject
    AntropometryChartDataMapper chartDataMapper;

    private Child child;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadResults();
    }

    public void init(@NonNull Child child) {
        this.child = child;
    }

    private void loadResults() {
        unsubscribeOnDestroy(
                getValues(child)
                        .map(values -> AntropometryChartState.builder()
                                .values(values)
                                .child(child)
                                .build())
                        .flatMap(state -> getLowValues(WhoNormRequest.builder()
                                .child(state.getChild())
                                .points(state.getValues())
                                .build())
                                .map(values -> state.toBuilder().lowValues(values).build()))
                        .flatMap(state -> getHighValues(WhoNormRequest.builder()
                                .child(state.getChild())
                                .points(state.getValues())
                                .build())
                                .map(values -> state.toBuilder().highValues(values).build()))
                        .map(state -> {
                            chartDataMapper.calculateData(state.getValues(), state.getLowValues(), state.getHighValues(), state.getChild().getBirthDate());
                            return state.toBuilder().data(chartDataMapper.getData()).build();
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showResults, this::onUnexpectedError));
    }

    protected abstract Observable<List<AntropometryPoint>> getValues(@NonNull Child child);

    protected abstract Observable<List<AntropometryPoint>> getLowValues(@NonNull WhoNormRequest request);

    protected abstract Observable<List<AntropometryPoint>> getHighValues(@NonNull WhoNormRequest request);
}
