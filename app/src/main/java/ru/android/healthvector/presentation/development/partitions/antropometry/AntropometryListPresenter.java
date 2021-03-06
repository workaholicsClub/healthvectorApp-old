package ru.android.healthvector.presentation.development.partitions.antropometry;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.ChildInteractor;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.requests.ChildResponse;
import ru.android.healthvector.domain.development.antropometry.AntropometryInteractor;
import ru.android.healthvector.domain.development.antropometry.data.Antropometry;
import ru.android.healthvector.presentation.core.adapters.DeletedItemsManager;
import ru.android.healthvector.presentation.development.partitions.core.BaseDevelopmentDiaryPresenter;

@InjectViewState
public class AntropometryListPresenter extends BaseDevelopmentDiaryPresenter<AntropometryListView> {
    @Inject
    ChildInteractor childInteractor;
    @Inject
    AntropometryInteractor antropometryInteractor;
    private DeletedItemsManager<Antropometry> deletedItemsManager = new DeletedItemsManager<>();
    private Disposable subscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(
                childInteractor.getActiveChild()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::requestData, this::onUnexpectedError));
    }

    private void requestData(@NonNull Child child) {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(antropometryInteractor.getAntropometryList(child)
                .map(antropometryList -> AntropometryListState.builder()
                        .child(child)
                        .antropometryList(antropometryList)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showAntropometryListState, this::onUnexpectedError));
    }

    public void edit(@NonNull Antropometry antropometry) {
        if (deletedItemsManager.check(antropometry)) {
            return;
        }
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                child -> getViewState().navigateToAntropometry(child, antropometry),
                                this::onUnexpectedError));
    }

    public void delete(@NonNull Antropometry antropometry) {
        if (deletedItemsManager.check(antropometry)) {
            return;
        }
        getViewState().confirmDelete(antropometry);
    }

    public void forceDelete(@NonNull Antropometry antropometry) {
        if (deletedItemsManager.checkAndAdd(antropometry)) {
            return;
        }
        unsubscribeOnDestroy(
                antropometryInteractor.delete(antropometry)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }

    public void showChart() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMapSingle(antropometryInteractor::hasChartData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    if (response.isHasData()) {
                                        getViewState().navigateToChart(response.getChild());
                                    } else {
                                        getViewState().noChartData();
                                    }
                                },
                                this::onUnexpectedError));
    }

    public void addAntropometry() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> antropometryInteractor.getDefaultAntropometry(child)
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    Antropometry antropometry = response.getResponse();
                                    getViewState().navigateToAntropometryAdd(antropometry.getChild(), antropometry);
                                },
                                this::onUnexpectedError));
    }

    public void addProfile() {
        getViewState().navigateToProfileAdd();
    }
}
