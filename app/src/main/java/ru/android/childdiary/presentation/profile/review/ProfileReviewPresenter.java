package ru.android.childdiary.presentation.profile.review;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class ProfileReviewPresenter extends BasePresenter<ProfileReviewView> {
    @Inject
    ChildInteractor childInteractor;

    private Child activeChild;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setActiveChild, this::onUnexpectedError));
    }

    private void setActiveChild(@NonNull Child child) {
        logger.debug("setActiveChild: " + child);
        if (!child.equals(activeChild)) {
            logger.debug("child changed");
            if (child == Child.NULL) {
                activeChild = null;
                logger.error("active child is null");
                return;
            } else {
                activeChild = child;
            }
            getViewState().showChild(activeChild);
        }
    }

    public void editChild() {
        if (activeChild == null) {
            logger.warn("editChild: active child is null");
            return;
        }
        getViewState().editChild(activeChild);
    }
}
