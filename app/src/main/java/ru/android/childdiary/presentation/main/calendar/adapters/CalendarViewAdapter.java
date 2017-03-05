package ru.android.childdiary.presentation.main.calendar.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import lombok.NonNull;
import ru.android.childdiary.data.types.Sex;

public abstract class CalendarViewAdapter extends BaseAdapter {
    public static final int DAYS_IN_WEEK = 7;
    private static final List<String> DAY_OF_WEEK_LIST;
    private static final List<Integer> DAY_OF_WEEK_INDEXES;
    private static final int DAY_OF_WEEK_TYPE = 0;
    private static final int DAY_OF_MONTH_TYPE = 1;

    static {
        String[] dayOfWeekArray = new DateFormatSymbols().getShortWeekdays();
        List<String> dayOfWeekList = Stream.of(dayOfWeekArray).map(String::toUpperCase).collect(Collectors.toList());
        DAY_OF_WEEK_LIST = Collections.unmodifiableList(dayOfWeekList);
        DAY_OF_WEEK_INDEXES = Collections.unmodifiableList(Calendar.getInstance().getFirstDayOfWeek() == Calendar.SUNDAY
                ? Arrays.asList(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY)
                : Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY));
    }

    protected final List<LocalDate> dates = new ArrayList<>();
    private final Context context;
    private LocalDate selectedDate, today;

    private Sex sex;

    public CalendarViewAdapter(Context context) {
        this.context = context;
        selectedDate = today = LocalDate.now();
        initCalendar(selectedDate);
    }

    public void setSex(Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            notifyDataSetChanged();
        }
    }

    public final LocalDate getSelectedDate() {
        return selectedDate;
    }

    public final void setSelectedDate(@NonNull LocalDate value) {
        if (!value.isEqual(selectedDate)) {
            selectedDate = value;
            selectedDateChanged();
            notifyDataSetChanged();
        }
    }

    protected void selectedDateChanged() {
    }

    @Override
    public Object getItem(int position) {
        if (getItemViewType(position) == DAY_OF_WEEK_TYPE) {
            return DAY_OF_WEEK_LIST.get(DAY_OF_WEEK_INDEXES.get(position));
        } else {
            return dates.get(position - DAYS_IN_WEEK);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return dates.size() + DAYS_IN_WEEK;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position < DAYS_IN_WEEK ? DAY_OF_WEEK_TYPE : DAY_OF_MONTH_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (getItemViewType(position) == DAY_OF_WEEK_TYPE) {
            DayOfWeekViewHolder viewHolder;
            if (view == null) {
                viewHolder = new DayOfWeekViewHolder();
                view = viewHolder.inflate(context);
                view.setTag(viewHolder);
            } else {
                viewHolder = (DayOfWeekViewHolder) view.getTag();
            }

            String item = (String) getItem(position);
            viewHolder.bind(context, position, item);
        } else {
            DayOfMonthViewHolder viewHolder;
            if (view == null) {
                viewHolder = new DayOfMonthViewHolder();
                view = viewHolder.inflate(context);
                view.setTag(viewHolder);
            } else {
                viewHolder = (DayOfMonthViewHolder) view.getTag();
            }

            LocalDate item = (LocalDate) getItem(position);
            viewHolder.bind(context, position, item);
            viewHolder.select(context, sex, selectedDate.isEqual(item));
        }

        return view;
    }

    protected abstract void initCalendar(LocalDate date);

    protected final int indexOfDayOfWeek(LocalDate date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.toDate());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int i = 0;
        while (i < DAYS_IN_WEEK && DAY_OF_WEEK_INDEXES.get(i) != dayOfWeek) {
            ++i;
        }

        return i;
    }
}
