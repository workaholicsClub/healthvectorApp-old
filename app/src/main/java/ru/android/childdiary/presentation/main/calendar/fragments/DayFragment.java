package ru.android.childdiary.presentation.main.calendar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.main.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.main.calendar.CalendarView;

public class DayFragment extends BaseMvpFragment<CalendarPresenter> implements CalendarView {
    @InjectPresenter
    CalendarPresenter presenter;

    public static DayFragment newInstance(Child child) {
        DayFragment fragment = new DayFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ExtraConstants.EXTRA_CHILD, child);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }
}
