package ru.android.childdiary.presentation.chart.antropometry.pages;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.data.AntropometryPoint;
import ru.android.childdiary.domain.interactors.development.antropometry.requests.WhoNormRequest;
import ru.android.childdiary.presentation.chart.antropometry.pages.core.AntropometryChartPresenter;

@InjectViewState
public class WeightChartPresenter extends AntropometryChartPresenter {
    @Override
    protected Observable<List<AntropometryPoint>> getValues(@NonNull Child child) {
        return antropometryInteractor.getWeights(child);
    }

    @Override
    protected Observable<List<AntropometryPoint>> getLowValues(@NonNull WhoNormRequest request) {
        return antropometryInteractor.getWeightLow(request);
    }

    @Override
    protected Observable<List<AntropometryPoint>> getHighValues(@NonNull WhoNormRequest request) {
        return antropometryInteractor.getWeightHigh(request);
    }
}
