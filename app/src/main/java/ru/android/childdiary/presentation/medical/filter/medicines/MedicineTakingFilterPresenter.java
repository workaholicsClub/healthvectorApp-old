package ru.android.childdiary.presentation.medical.filter.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListFilter;
import ru.android.childdiary.presentation.core.BasePresenter;

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
