package ru.android.childdiary.presentation.core;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;

public abstract class AppPartitionPresenter<V extends AppPartitionView> extends BasePresenter<V> {
    @Inject
    protected ChildInteractor childInteractor;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(child -> logger.debug("showChild: " + child))
                .subscribe(getViewState()::showChild, this::onUnexpectedError));
    }
}
