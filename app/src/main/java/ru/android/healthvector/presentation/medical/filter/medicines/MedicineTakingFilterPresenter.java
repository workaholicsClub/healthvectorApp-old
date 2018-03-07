package ru.android.healthvector.presentation.medical.filter.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.medical.MedicineTakingInteractor;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListFilter;
import ru.android.healthvector.presentation.core.BasePresenter;

@InjectViewState
public class MedicineTakingFilterPresenter extends BasePresenter<MedicineTakingFilterView> {
    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void setFilter(@NonNull GetMedicineTakingListFilter filter) {
        unsubscribeOnDestroy(medicineTakingInteractor.setSelectedFilterValueObservable(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(f -> {
                }, this::onUnexpectedError));
    }
}
