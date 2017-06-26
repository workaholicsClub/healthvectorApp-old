package ru.android.childdiary.presentation.core;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.joda.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;

public abstract class AppPartitionFragment extends BaseMvpFragment implements AppPartitionView {
    protected static final String TAG_FILTER = "TAG_FILTER";

    @Getter(AccessLevel.PROTECTED)
    private LocalDate selectedDate;

    @Getter(AccessLevel.PROTECTED)
    private Child child;

    @Nullable
    @Getter(AccessLevel.PROTECTED)
    private Sex sex;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        AppPartitionArguments arguments = (AppPartitionArguments) getArguments().getSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS);
        selectedDate = arguments.getSelectedDate();
        child = arguments.getChild();
        sex = child.getSex();
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUi();
        themeChanged();
        showSelectedDate(selectedDate);
        showChild(child);
    }

    protected abstract void setupUi();

    @CallSuper
    protected void themeChanged() {
        logger.debug("setup theme: " + sex);
    }

    @Override
    @CallSuper
    public void showSelectedDate(@NonNull LocalDate selectedDate) {
        this.selectedDate = selectedDate;
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

    public void showFilter() {
    }

    protected Fragment putArguments(Fragment fragment) {
        AppPartitionArguments arguments = AppPartitionArguments.builder()
                .child(getChild())
                .selectedDate(getSelectedDate())
                .build();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraConstants.EXTRA_APP_PARTITION_ARGUMENTS, arguments);
        fragment.setArguments(bundle);
        return fragment;
    }
}
