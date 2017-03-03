package ru.android.childdiary.presentation.main.calendar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.main.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.main.calendar.CalendarView;
import ru.android.childdiary.presentation.main.calendar.adapters.WeekViewAdapter;

public class WeekFragment extends BaseMvpFragment<CalendarPresenter> implements CalendarView,
        AdapterView.OnItemClickListener {
    @InjectPresenter
    CalendarPresenter presenter;

    @BindView(R.id.gridViewCalendar)
    GridView gridViewCalendar;

    public static WeekFragment newInstance(Child child) {
        WeekFragment fragment = new WeekFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ExtraConstants.EXTRA_CHILD, child);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridViewCalendar.setAdapter(new WeekViewAdapter(getActivity()));
        gridViewCalendar.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showToast(String.valueOf(position));
    }
}
