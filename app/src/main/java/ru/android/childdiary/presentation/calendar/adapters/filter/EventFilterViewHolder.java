package ru.android.childdiary.presentation.calendar.adapters.filter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.childdiary.utils.strings.StringUtils;

public class EventFilterViewHolder extends BaseRecyclerViewHolder<EventType> implements FieldCheckBoxView.FieldCheckBoxListener {
    @BindView(R.id.checkBoxView)
    FieldCheckBoxView checkBoxView;

    @NonNull
    private ItemSelectedListener listener;

    public EventFilterViewHolder(View itemView, @NonNull ItemSelectedListener listener) {
        super(itemView);
        this.listener = listener;
    }

    @Override
    public void bind(Context context, @Nullable Sex sex, EventType item) {
        super.bind(context, sex, item);
        checkBoxView.setSex(sex);
        checkBoxView.setText(StringUtils.eventType(context, item));
        checkBoxView.setFieldCheckBoxListener(this);
    }

    public void setSelected(boolean selected) {
        checkBoxView.setChecked(selected);
    }

    @Override
    public void onChecked() {
        listener.onItemSelected(item, checkBoxView.isChecked());
    }
}
