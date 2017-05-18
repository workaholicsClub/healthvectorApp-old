package ru.android.childdiary.presentation.medical.edit.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.joda.time.DateTime;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertDoctorVisitRequest;
import ru.android.childdiary.presentation.medical.core.BaseEditItemPresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class EditDoctorVisitPresenter extends BaseEditItemPresenter<EditDoctorVisitView, DoctorVisit> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void update(@NonNull DoctorVisit doctorVisit) {
        showProgressUpdate(doctorVisit);
        unsubscribeOnDestroy(
                doctorVisitInteractor.updateDoctorVisit(UpsertDoctorVisitRequest.builder()
                        .doctorVisit(doctorVisit)
                        .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(updated -> logger.debug("updated: " + updated))
                        .doOnNext(added -> hideProgressUpdate(doctorVisit))
                        .doOnError(throwable -> hideProgressUpdate(doctorVisit))
                        .subscribe(response -> getViewState().updated(response.getDoctorVisit(), response.getAddedEventsCount()),
                                this::onUnexpectedError));
    }

    private void showProgressUpdate(@NonNull DoctorVisit doctorVisit) {
        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
            getViewState().showGeneratingEvents(true);
        }
    }

    private void hideProgressUpdate(@NonNull DoctorVisit doctorVisit) {
        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
            getViewState().showGeneratingEvents(false);
        }
    }

    @Override
    public void delete(@NonNull DoctorVisit doctorVisit) {
        if (ObjectUtils.isTrue(doctorVisit.getIsExported())) {
            getViewState().askDeleteConnectedEventsOrNot(doctorVisit);
        } else {
            deleteOneItem(doctorVisit);
        }
    }

    @Override
    public void deleteOneItem(@NonNull DoctorVisit doctorVisit) {
        unsubscribeOnDestroy(doctorVisitInteractor.deleteDoctorVisit(DeleteDoctorVisitRequest.builder()
                .doctorVisit(doctorVisit)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> getViewState().deleted(doctorVisit), this::onUnexpectedError));
    }

    @Override
    public void deleteWithConnectedEvents(@NonNull DoctorVisit doctorVisit) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(doctorVisitInteractor.deleteDoctorVisitWithEvents(DeleteDoctorVisitEventsRequest.builder()
                .doctorVisit(doctorVisit)
                .linearGroup(null)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(response -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(response -> getViewState().deleted(doctorVisit), this::onUnexpectedError));
    }

    @Override
    public void complete(@NonNull DoctorVisit item) {
        if (item.getFinishDateTime() == null) {
            getViewState().askCompleteFromDate(item, DateTime.now());
        } else {
            logger.error("already completed: " + item);
        }
    }

    @Override
    public void completeWithoutDeletion(@NonNull DoctorVisit doctorVisit, @NonNull DateTime dateTime) {
        unsubscribeOnDestroy(doctorVisitInteractor.completeDoctorVisit(CompleteDoctorVisitRequest.builder()
                .doctorVisit(doctorVisit)
                .dateTime(dateTime)
                .delete(false)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> getViewState().completed(doctorVisit), this::onUnexpectedError));
    }

    @Override
    public void completeAndDeleteFromDate(@NonNull DoctorVisit doctorVisit, @NonNull DateTime dateTime) {
        getViewState().showDeletingEvents(true);
        unsubscribeOnDestroy(doctorVisitInteractor.completeDoctorVisit(CompleteDoctorVisitRequest.builder()
                .doctorVisit(doctorVisit)
                .dateTime(dateTime)
                .delete(true)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(count -> getViewState().showDeletingEvents(false))
                .doOnError(throwable -> getViewState().showDeletingEvents(false))
                .subscribe(count -> getViewState().completed(doctorVisit), this::onUnexpectedError));
    }
}
