package ru.android.childdiary.presentation.profile.edit;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    public void listenForUpdate(@NonNull Observable<Child> childObservable) {
        unsubscribeOnDestroy(childInteractor.controlDoneButton(childObservable)
                .subscribe(enabled -> getViewState().setButtonDoneEnabled(enabled), this::onUnexpectedError));
    }

    public void addChild(Child child) {
        unsubscribeOnDestroy(childInteractor.add(child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddChild, this::onUnexpectedError));
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof ChildValidationException) {
            handleValidationResult((ChildValidationException) e);
        } else {
            super.onUnexpectedError(e);
        }
    }

    private void handleValidationResult(ChildValidationException e) {
        List<ChildValidationResult> results = e.getValidationResults();
        if (!results.isEmpty()) {
            String msg = Stream.of(results).map(ChildValidationResult::toString).collect(Collectors.joining("\n"));
            getViewState().showValidationErrorMessage(msg);
            boolean shouldFocus = true;
            for (int i = 0; i < results.size(); ++i) {
                ChildValidationResult result = results.get(i);
                boolean valid = result.isValid();
                switch (result.getFieldType()) {
                    case IMAGE:
                        // необязательное поле
                        break;
                    case NAME:
                        getViewState().nameValidated(valid, shouldFocus);
                        shouldFocus = false;
                        break;
                    case SEX:
                        getViewState().sexValidated(valid, shouldFocus);
                        break;
                    case BIRTH_DATE:
                        getViewState().birthDateValidated(valid, shouldFocus);
                        break;
                    case BIRTH_TIME:
                        // необязательное поле
                        break;
                    case BIRTH_HEIGHT:
                        getViewState().birthHeightValidated(valid, shouldFocus);
                        shouldFocus = false;
                        break;
                    case BIRTH_WEIGHT:
                        getViewState().birthWeightValidated(valid, shouldFocus);
                        shouldFocus = false;
                        break;
                }
            }
        }
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
