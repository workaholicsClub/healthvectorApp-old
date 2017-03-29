package ru.android.childdiary.presentation.core;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;

public abstract class AppPartitionFragment extends BaseMvpFragment implements AppPartitionView {
    @NonNull
    @Getter(AccessLevel.PROTECTED)
    private Child child;

    @Nullable
    @Getter(AccessLevel.PROTECTED)
    private Sex sex;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        child = (Child) getArguments().getSerializable(ExtraConstants.EXTRA_CHILD);
        sex = child.getSex();
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUi();
        themeChanged();
    }

    protected abstract void setupUi();

    @CallSuper
    protected void themeChanged() {
        logger.debug("setup theme: " + sex);
    }

    @Override
    @CallSuper
    public void showChild(@NonNull Child child) {
        this.child = child;
        if (sex != child.getSex()) {
            logger.debug("theme switched");
            sex = child.getSex();
            themeChanged();
        }
    }
}
