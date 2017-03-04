package ru.android.childdiary.presentation.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.StringUtils;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    @Inject
    ChildInteractor childInteractor;

    private List<Child> childList;
    private Child activeChild;

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
    }

    private void onGetChildList(@NonNull List<Child> childList) {
        logger.debug("onGetChildList: " + StringUtils.toString(childList));
        boolean isFirstTime = this.childList == null;
        this.childList = childList;
        requestActiveChild();
        getViewState().showChildList(childList);
        if (childList.isEmpty() && isFirstTime) {
            getViewState().addChild();
        }
    }

    public void requestActiveChild() {
        unsubscribeOnDestroy(childInteractor.getActiveChild(childList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setActiveChild, this::onUnexpectedError));
    }

    public void toggleChild(@Nullable Long id) {
        unsubscribeOnDestroy(childInteractor.setActiveChild(childList, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setActiveChild, this::onUnexpectedError));
    }

    private void setActiveChild(@NonNull Child child) {
        logger.debug("setActiveChild");
        if (child == Child.NULL) {
            activeChild = null;
        } else {
            activeChild = child;
        }
        getViewState().setActive(activeChild);
    }

    public void addChild() {
        getViewState().addChild();
    }

    public void editChild() {
        if (activeChild == null) {
            logger.warn("editChild: active child is null");
            return;
        }
        getViewState().editChild(activeChild);
    }

    public void reviewChild() {
        if (activeChild == null) {
            logger.warn("reviewChild: active child is null");
            return;
        }
        getViewState().reviewChild(activeChild);
    }

    public void deleteChild() {
        if (activeChild == null) {
            logger.warn("deleteChild: active child is null");
            return;
        }
        unsubscribeOnDestroy(childInteractor.delete(activeChild)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDeleteChild, this::onUnexpectedError));
    }

    private void onDeleteChild(Child child) {
        logger.debug("onDeleteChild: " + child);
    }
}
