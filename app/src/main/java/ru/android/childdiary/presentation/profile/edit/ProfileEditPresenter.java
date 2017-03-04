package ru.android.childdiary.presentation.profile.edit;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.child.ChildValidationException;
import ru.android.childdiary.domain.interactors.child.ChildValidationResult;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class ProfileEditPresenter extends BasePresenter<ProfileEditView> {
    @Inject
    ChildInteractor childInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public Disposable listenForDoneButtonUpdate(@NonNull Observable<Child> childObservable) {
        return childInteractor.controlDoneButton(childObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }

    public Disposable listenForFieldsUpdate(@NonNull Observable<Child> childObservable) {
        return childInteractor.controlFields(childObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof ChildValidationException) {
            List<ChildValidationResult> results = ((ChildValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("child validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Stream.of(results)
                    .filter(ChildValidationResult::notValid)
                    .map(ChildValidationResult::toString)
                    .collect(Collectors.joining("\n"));
            getViewState().showValidationErrorMessage(msg);
            handleValidationResult(results);
        } else {
            super.onUnexpectedError(e);
        }
    }

    private void handleValidationResult(List<ChildValidationResult> results) {
        for (ChildValidationResult result : results) {
            boolean valid = result.isValid();
            switch (result.getFieldType()) {
                case IMAGE:
                    // необязательное поле
                    break;
                case NAME:
                    getViewState().nameValidated(valid);
                    break;
                case SEX:
                    getViewState().sexValidated(valid);
                    break;
                case BIRTH_DATE:
                    getViewState().birthDateValidated(valid);
                    break;
                case BIRTH_TIME:
                    // необязательное поле
                    break;
                case BIRTH_HEIGHT:
                    getViewState().birthHeightValidated(valid);
                    break;
                case BIRTH_WEIGHT:
                    getViewState().birthWeightValidated(valid);
                    break;
            }
        }
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
