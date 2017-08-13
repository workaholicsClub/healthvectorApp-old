package ru.android.childdiary.presentation.development.partitions.antropometry.edit;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.development.antropometry.data.Antropometry;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryPresenter;

@InjectViewState
public class EditAntropometryPresenter extends AntropometryPresenter<EditAntropometryView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void update(@NonNull Antropometry antropometry) {
        unsubscribeOnDestroy(
                antropometryInteractor.update(antropometry)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::updated, this::onUnexpectedError));
    }

    public void delete(@NonNull Antropometry antropometry) {
        getViewState().confirmDelete(antropometry);
    }

    public void forceDelete(@NonNull Antropometry antropometry) {
        unsubscribeOnDestroy(
                antropometryInteractor.delete(antropometry)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }
}
