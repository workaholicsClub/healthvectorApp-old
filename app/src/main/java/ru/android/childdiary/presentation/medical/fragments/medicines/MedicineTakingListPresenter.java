package ru.android.childdiary.presentation.medical.fragments.medicines;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class MedicineTakingListPresenter extends AppPartitionPresenter<MedicineTakingListView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void requestMedicineDetail() {
        getViewState().navigateToMedicineTaking();
    }
}
