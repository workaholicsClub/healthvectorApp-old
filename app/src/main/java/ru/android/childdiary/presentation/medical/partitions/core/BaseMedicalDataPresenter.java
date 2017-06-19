package ru.android.childdiary.presentation.medical.partitions.core;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.medical.filter.adapters.Chips;

public abstract class BaseMedicalDataPresenter<V extends BaseMedicalDataView> extends BasePresenter<V> {
    @Inject
    protected ChildInteractor childInteractor;

    public final void requestFilterDialog() {
        unsubscribeOnDestroy(childInteractor.getActiveChildOnce()
                .flatMapSingle(this::hasDataToFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hasDataToFilter -> {
                    if (hasDataToFilter) {
                        showFilterDialog();
                    } else {
                        getViewState().showNoDataToFilter();
                    }
                }, this::onUnexpectedError));
    }

    protected abstract Single<Boolean> hasDataToFilter(@NonNull Child child);

    protected abstract void showFilterDialog();

    public abstract void setFilter(@NonNull List<Chips> chips);
}