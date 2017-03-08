package ru.android.childdiary.presentation.main.calendar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpFragment;
import ru.android.childdiary.presentation.main.calendar.CalendarPresenter;
import ru.android.childdiary.presentation.main.calendar.CalendarView;
import ru.android.childdiary.presentation.main.calendar.adapters.CalendarViewAdapter;

public abstract class CalendarFragment<Adapter extends CalendarViewAdapter> extends BaseMvpFragment<CalendarPresenter> implements CalendarView,
        AdapterView.OnItemClickListener, CalendarViewAdapter.OnSelectedDateChanged {
    @BindView(R.id.calendarTitle)
    protected TextView calendarTitle;

    @InjectPresenter
    CalendarPresenter presenter;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.gridViewCalendar)
    GridView gridViewCalendar;

    private Adapter adapter;
    private Sex sex;

    @Override
    @Nullable
    protected Sex getSex() {
        return sex;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridViewCalendar.setOnItemClickListener(this);
        adapter = getCalendarViewAdapter();
        gridViewCalendar.setAdapter(adapter);
        updateTitle(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getItem(position) instanceof LocalDate) {
            LocalDate date = (LocalDate) adapter.getItem(position);
            adapter.setSelectedDate(date);
        }
    }

    @Override
    public void setActive(@Nullable Child child) {
        textView.setText(child == null ? "no active child" : child.getName());
        sex = child == null ? null : child.getSex();
        adapter.setSex(sex);
    }

    protected abstract Adapter getCalendarViewAdapter();

    @OnClick(R.id.left)
    void moveLeft() {
        adapter.moveLeft();
    }

    @OnClick(R.id.right)
    void moveRight() {
        adapter.moveRight();
    }

    @Override
    public final void onSelectedDateChanged() {
        updateTitle(adapter);
    }

    protected abstract void updateTitle(Adapter adapter);
}
