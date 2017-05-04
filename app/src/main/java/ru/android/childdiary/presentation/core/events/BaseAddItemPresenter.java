package ru.android.childdiary.presentation.core.events;

import android.support.annotation.NonNull;

import java.io.Serializable;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialog;

public abstract class BaseAddItemPresenter<V extends BaseAddItemView<T>, T extends Serializable> extends BasePresenter<V> {
    @Inject
    ChildInteractor childInteractor;

    public abstract void add(@NonNull T item);

    public void requestTimeDialog(String tag, TimeDialog.Parameters parameters) {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(child -> getViewState().showTimeDialog(tag, child, parameters), this::onUnexpectedError));
    }
}
