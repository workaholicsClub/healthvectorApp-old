package ru.android.healthvector.presentation.chart.antropometry.pages;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import io.reactivex.Observable;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.antropometry.data.AntropometryPoint;
import ru.android.healthvector.domain.development.antropometry.requests.WhoNormRequest;
import ru.android.healthvector.presentation.chart.antropometry.pages.core.AntropometryChartPresenter;

@InjectViewState
public class HeightChartPresenter extends AntropometryChartPresenter {
    @Override
    protected Observable<List<AntropometryPoint>> getValues(@NonNull Child child) {
        return antropometryInteractor.getHeights(child);
    }

    @Override
    protected Observable<List<AntropometryPoint>> getLowValues(@NonNull WhoNormRequest request) {
        return antropometryInteractor.getHeightLow(request);
    }

    @Override
    protected Observable<List<AntropometryPoint>> getHighValues(@NonNull WhoNormRequest request) {
        return antropometryInteractor.getHeightHigh(request);
    }
}
