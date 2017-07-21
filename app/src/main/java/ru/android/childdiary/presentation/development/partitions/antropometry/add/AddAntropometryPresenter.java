package ru.android.childdiary.presentation.development.partitions.antropometry.add;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;
import ru.android.childdiary.presentation.development.partitions.antropometry.core.AntropometryPresenter;

@InjectViewState
public class AddAntropometryPresenter extends AntropometryPresenter<AddAntropometryView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void add(@NonNull Antropometry antropometry) {
        unsubscribeOnDestroy(
                antropometryInteractor.add(child, antropometry)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::added, this::onUnexpectedError));
    }
}
