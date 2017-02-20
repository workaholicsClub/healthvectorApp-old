package ru.android.childdiary.presentation.main;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    @Inject
    ChildInteractor childInteractor;

    private Child activeChild;
    private List<Child> childList;

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
                .subscribe(this::onChildListLoaded, this::onUnexpectedError));
    }

    private void onChildListLoaded(List<Child> childList) {
        // TODO: брать активный профиль из настроек
        activeChild = childList.isEmpty() ? null : childList.get(0);
        this.childList = childList;
        getViewState().childListLoaded(activeChild, childList);
        if (childList.isEmpty()) {
            getViewState().addChild();
        }
    }

    public void addChild() {
        getViewState().addChild();
    }

    public void editChild() {
        if (activeChild == null) {
            logger.warn("editChild: active child is null");
        }
        getViewState().editChild(activeChild);
    }

    public void toggleChild(long id) {
        Child child = Stream.of(childList).filter(c -> c.getId() == id).findFirst().orElse(null);
        if (child == null) {
            logger.warn("editChild: child with id=" + id + " not found");
        }
        getViewState().setActive(child);
    }
}
