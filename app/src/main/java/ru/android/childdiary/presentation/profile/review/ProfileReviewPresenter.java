package ru.android.childdiary.presentation.profile.review;

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

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void loadChild(Long id) {
        unsubscribeOnDestroy(childInteractor.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onChildLoaded, this::onUnexpectedError));
    }

    private void onChildLoaded(Child child) {
        logger.debug("onChildLoaded");
        getViewState().childLoaded(child);
    }
}
