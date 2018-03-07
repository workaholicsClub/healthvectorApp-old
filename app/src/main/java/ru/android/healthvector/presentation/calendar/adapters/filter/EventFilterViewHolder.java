package ru.android.healthvector.presentation.calendar.adapters.filter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.healthvector.presentation.core.fields.widgets.FieldCheckBoxView;
import ru.android.healthvector.utils.strings.StringUtils;

class EventFilterViewHolder extends BaseRecyclerViewHolder<EventType> implements FieldCheckBoxView.FieldCheckBoxListener {
    @BindView(R.id.checkBoxView)
    FieldCheckBoxView checkBoxView;

    @NonNull
    private ItemSelectedListener listener;

    public EventFilterViewHolder(View itemView, @NonNull ItemSelectedListener listener) {
        super(itemView);
        this.listener = listener;
    }

    @Override
    protected void bind(Context context, @Nullable Sex sex) {
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
