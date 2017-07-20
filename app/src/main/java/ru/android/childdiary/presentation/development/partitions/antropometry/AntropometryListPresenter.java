package ru.android.childdiary.presentation.development.partitions.antropometry;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryInteractor;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryPresenter;

@InjectViewState
public class AntropometryListPresenter extends BaseDevelopmentDiaryPresenter<AntropometryListView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    AntropometryInteractor antropometryInteractor;

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
        subscription = unsubscribeOnDestroy(antropometryInteractor.getAll(child)
                .map(antropometryList -> AntropometryListState.builder()
                        .child(child)
                        .antropometryList(antropometryList)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showAntropometryListState, this::onUnexpectedError));
    }

    public void edit(@NonNull Antropometry antropometry) {
        getViewState().navigateToAntropometry(antropometry);
    }

    public void delete(@NonNull Antropometry antropometry) {
        getViewState().confirmDelete(antropometry);
    }

    public void forceDelete(@NonNull Antropometry antropometry) {
        unsubscribeOnDestroy(
                antropometryInteractor.delete(antropometry)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }
}
