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

    private Child child;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void loadChild(@NonNull Long id) {
        unsubscribeOnDestroy(childInteractor.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetChild, this::onUnexpectedError));
    }

    private void onGetChild(@NonNull Child child) {
        logger.debug("onGetChild");
        this.child = child;
        getViewState().showChild(child);
    }

    public void editChild() {
        if (child == null) {
            logger.warn("editChild: child is null");
            return;
        }
        getViewState().editChild(child);
    }
}
