package ru.android.childdiary.presentation.chart.antropometry.pages.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

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
import ru.android.childdiary.presentation.chart.core.ChartPresenter;

public abstract class AntropometryChartPresenter extends ChartPresenter<AntropometryChartView> {
    @Inject
    protected AntropometryInteractor antropometryInteractor;

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
                                .build())
                        .flatMap(state -> getLowValues(WhoNormRequest.builder()
                                .child(child)
                                .minDate(extractMinDate(state))
                                .maxDate(extractMaxDate(state))
                                .build())
                                .map(values -> state.toBuilder().lowValues(values).build()))
                        .flatMap(state -> getHighValues(WhoNormRequest.builder()
                                .child(child)
                                .minDate(extractMinDate(state))
                                .maxDate(extractMaxDate(state))
                                .build())
                                .map(values -> state.toBuilder().highValues(values).build()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showResults, this::onUnexpectedError));
    }

    protected abstract Observable<List<AntropometryPoint>> getValues(@NonNull Child child);

    protected abstract Observable<List<AntropometryPoint>> getLowValues(@NonNull WhoNormRequest request);

    protected abstract Observable<List<AntropometryPoint>> getHighValues(@NonNull WhoNormRequest request);

    @Nullable
    private LocalDate extractMinDate(@NonNull AntropometryChartState state) {
        return state.getValues().isEmpty() ? null : state.getValues().get(0).getDate();
    }

    @Nullable
    private LocalDate extractMaxDate(@NonNull AntropometryChartState state) {
        return state.getValues().isEmpty() ? null : state.getValues().get(state.getValues().size() - 1).getDate();
    }
}
