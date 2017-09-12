package ru.android.childdiary.presentation.calendar.adapters.calendar;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.BaseTwoTypesAdapter;
import ru.android.childdiary.utils.strings.DateUtils;

public abstract class CalendarViewAdapter extends BaseTwoTypesAdapter<DayOfWeekViewHolder, String, DayOfMonthViewHolder, LocalDate> {
    public static final int DAYS_IN_WEEK = 7;

    protected final List<LocalDate> dates = new ArrayList<>();

    @Getter
    protected LocalDate selectedDate = LocalDate.now();

    @Getter
    protected Sex sex;

    @Getter
    private boolean isSelected;

    private OnSelectedDateChanged onSelectedDateChanged;
    private String[] weekdaysNames;
    private int[] weekdaysIndexes;

    public CalendarViewAdapter(Context context, OnSelectedDateChanged onSelectedDateChanged) {
        super(context);
        this.onSelectedDateChanged = onSelectedDateChanged;
        initCalendar(selectedDate);
    }

    protected final int indexOfDayOfWeek(LocalDate date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.toDate());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int i = 0;
        while (i < DAYS_IN_WEEK && weekdaysIndexes[i] != dayOfWeek) {
            ++i;
        }
        return i;
    }

    private void initWeekdaysInfo() {
        boolean isSundayFirstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek() == Calendar.SUNDAY;
        weekdaysNames = DateUtils.weekdayShortNames(getContext());
        if (isSundayFirstDayOfWeek) {
            String[] newWeekdaysNames = new String[weekdaysNames.length];
            System.arraycopy(weekdaysNames, 0, newWeekdaysNames, 1, weekdaysNames.length - 1);
            newWeekdaysNames[0] = weekdaysNames[weekdaysNames.length - 1];
            weekdaysNames = newWeekdaysNames;
        }
        weekdaysIndexes = isSundayFirstDayOfWeek
                ? new int[]{
                Calendar.SUNDAY,
                Calendar.MONDAY,
                Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY}
                : new int[]{
                Calendar.MONDAY,
                Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY,
                Calendar.SUNDAY};
    }

    @CallSuper
    protected void initCalendar(@NonNull LocalDate date) {
        initWeekdaysInfo();
    }

    public final void setSelectedDate(@NonNull LocalDate value) {
        setSelectedDate(value, true);
    }

    public final void setSelectedDate(@NonNull LocalDate value, boolean fire) {
        if (value.isEqual(selectedDate)) {
            isSelected = true;
            notifyDataSetChanged();
        } else {
            isSelected = true;
            selectedDate = value;
            selectedDateChanged();
            notifyDataSetChanged();
            if (fire && onSelectedDateChanged != null) {
                onSelectedDateChanged.onSelectedDateChanged();
            }
        }
    }

    protected void selectedDateChanged() {
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return dates.size() + DAYS_IN_WEEK;
    }

    @Override
    protected boolean isItemOfFirstType(int position) {
        return position < DAYS_IN_WEEK;
    }

    @Override
    protected String getFirstTypeItem(int position) {
        return weekdaysNames[position];
    }

    @Override
    protected LocalDate getSecondTypeItem(int position) {
        return dates.get(position - DAYS_IN_WEEK);
    }

    @Override
    @LayoutRes
    protected int getFirstTypeLayoutResourceId() {
        return R.layout.grid_cell_day_of_week;
    }

    @Override
    @LayoutRes
    protected int getSecondTypeLayoutResourceId() {
        return R.layout.grid_cell_day_of_month;
    }

    @Override
    protected DayOfWeekViewHolder createFirstTypeViewHolder(View view) {
        return new DayOfWeekViewHolder(view);
    }

    @Override
    protected DayOfMonthViewHolder createSecondTypeViewHolder(View view) {
        return new DayOfMonthViewHolder(view);
    }

    @Override
    protected void bindSecond(int position, LocalDate item, DayOfMonthViewHolder viewHolder) {
        super.bindSecond(position, item, viewHolder);
        viewHolder.select(this, item);
    }

    public abstract void moveLeft();

    public void moveToday() {
        setSelectedDate(LocalDate.now());
    }

    public abstract void moveRight();

    public interface OnSelectedDateChanged {
        void onSelectedDateChanged();
    }
}
