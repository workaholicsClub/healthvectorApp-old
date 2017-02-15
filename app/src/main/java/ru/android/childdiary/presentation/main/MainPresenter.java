package ru.android.childdiary.presentation.main;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.AntropometryInteractor;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    AntropometryInteractor antropometryInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(childInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetChildList, this::onUnexpectedError));

        unsubscribeOnDestroy(antropometryInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetAntropometryList, this::onUnexpectedError));
    }

    private void onGetChildList(List<Child> childList) {
        getViewState().showChildList(childList);
    }

    private void onGetAntropometryList(List<Antropometry> antropometryList) {
        getViewState().showAntropometryList(antropometryList);
    }

    void addChild(Child child) {
        unsubscribeOnDestroy(childInteractor.add(child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> getViewState().showToast("added " + item), this::onUnexpectedError));
    }

    void addAntropometry(Antropometry antropometry) {
        unsubscribeOnDestroy(antropometryInteractor.add(antropometry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> getViewState().showToast("added " + item), this::onUnexpectedError));
    }
}
