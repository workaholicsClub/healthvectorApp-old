package ru.android.childdiary.presentation.profile.edit;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class ProfileEditPresenter extends BasePresenter<ProfileEditView> {
    @Inject
    ChildInteractor childInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void addChild(Child child) {
        unsubscribeOnDestroy(childInteractor.add(child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddChild, this::onUnexpectedError));
    }

    private void onAddChild(Child child) {
        logger.debug("onAddChild");
        getViewState().childAdded(child);
    }

    public void updateChild(Child child) {
        unsubscribeOnDestroy(childInteractor.update(child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUpdateChild, this::onUnexpectedError));
    }

    private void onUpdateChild(Child child) {
        logger.debug("onUpdateChild");
        getViewState().childUpdated(child);
    }
}
