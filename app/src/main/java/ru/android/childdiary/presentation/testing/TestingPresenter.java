package ru.android.childdiary.presentation.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.presentation.core.AppPartitionArguments;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class TestingPresenter extends BasePresenter<TestingView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    TestingInteractor testingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void initTest(@NonNull TestType testType, @NonNull Child child) {
        getViewState().showStart(AppPartitionArguments.builder()
                .child(child)
                .selectedDate(null)
                .build());
    }
}
