package ru.android.childdiary.presentation.main.calendar;

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
public class CalendarPresenter extends BasePresenter<CalendarView> {
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

        requestActiveChild();
    }

    private void requestActiveChild() {
        unsubscribeOnDestroy(childInteractor.getActiveChild()
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
}
