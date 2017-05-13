package ru.android.childdiary.presentation.medical.edit.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.requests.DeleteEventsRequest;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.presentation.medical.core.BaseEditItemPresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class EditDoctorVisitPresenter extends BaseEditItemPresenter<EditDoctorVisitView, DoctorVisit> {
    @Inject
    CalendarInteractor calendarInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void update(@NonNull DoctorVisit doctorVisit) {
        unsubscribeOnDestroy(
                doctorVisitInteractor.updateDoctorVisit(doctorVisit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(updated -> logger.debug("updated: " + updated))
                        .subscribe(getViewState()::updated, this::onUnexpectedError));
    }

    @Override
    public void delete(@NonNull DoctorVisit doctorVisit) {
        if (ObjectUtils.isTrue(doctorVisit.getExported())) {
            getViewState().askDeleteConnectedEventsOrNot(doctorVisit);
        } else {
            deleteDoctorVisit(doctorVisit);
        }
    }

    public void deleteDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        unsubscribeOnDestroy(doctorVisitInteractor.deleteDoctorVisit(doctorVisit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }

    public void deleteDoctorVisitAndConnectedEvents(@NonNull DoctorVisit doctorVisit) {
        unsubscribeOnDestroy(calendarInteractor.delete(DeleteEventsRequest.builder()
                .deleteType(DeleteEventsRequest.DeleteType.DELETE_ALL_DOCTOR_VISIT_EVENTS)
                .doctorVisit(doctorVisit)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> getViewState().deleted(doctorVisit), this::onUnexpectedError));
    }
}
