package ru.android.childdiary.presentation.main.calendar.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.main.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.main.calendar.CalendarView;

public class DayFragment extends BaseMvpFragment<CalendarPresenter> implements CalendarView {
    @InjectPresenter
    CalendarPresenter presenter;

    @Override
    @Nullable
    protected Sex getSex() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void setActive(@NonNull Child child) {
    }

    @Override
    public void setSelected(@NonNull LocalDate date) {
    }

    @Override
    public void showEvents(@NonNull List<MasterEvent> events) {
    }
}
