package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.BaseTwoTypesAdapter;

public abstract class CalendarViewAdapter extends BaseTwoTypesAdapter<DayOfWeekViewHolder, String, DayOfMonthViewHolder, LocalDate> {
    public static final int DAYS_IN_WEEK = 7;
    private static final List<String> DAY_OF_WEEK_LIST;
    private static final List<Integer> DAY_OF_WEEK_INDEXES;

    static {
        String[] dayOfWeekArray = new DateFormatSymbols().getShortWeekdays();
        List<String> dayOfWeekList = Stream.of(dayOfWeekArray).map(String::toUpperCase).collect(Collectors.toList());
        DAY_OF_WEEK_LIST = Collections.unmodifiableList(dayOfWeekList);
        DAY_OF_WEEK_INDEXES = Collections.unmodifiableList(Calendar.getInstance().getFirstDayOfWeek() == Calendar.SUNDAY
                ? Arrays.asList(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY)
                : Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY));
    }

    protected final List<LocalDate> dates = new ArrayList<>();
    @Getter
    protected LocalDate selectedDate = LocalDate.now();
    @Getter
    protected Sex sex;
    private OnSelectedDateChanged onSelectedDateChanged;

    public CalendarViewAdapter(Context context, OnSelectedDateChanged onSelectedDateChanged) {
        super(context);
        this.onSelectedDateChanged = onSelectedDateChanged;
        initCalendar(selectedDate);
    }

    protected static int indexOfDayOfWeek(LocalDate date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.toDate());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int i = 0;
        while (i < DAYS_IN_WEEK && DAY_OF_WEEK_INDEXES.get(i) != dayOfWeek) {
            ++i;
        }

        return i;
    }

    protected abstract void initCalendar(@NonNull LocalDate date);

    public final void setSelectedDate(@NonNull LocalDate value) {
        setSelectedDate(value, true);
    }

    public final void setSelectedDate(@NonNull LocalDate value, boolean fire) {
        if (!value.isEqual(selectedDate)) {
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
        return DAY_OF_WEEK_LIST.get(DAY_OF_WEEK_INDEXES.get(position));
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

    public abstract void moveRight();

    public interface OnSelectedDateChanged {
        void onSelectedDateChanged();
    }
}
