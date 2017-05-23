package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import org.joda.time.LocalDate;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.DateUtils;

public class FieldDateFilterView extends FieldDialogView<LocalDate> {
    @BindView(R.id.title)
    TextView textViewTitle;

    public FieldDateFilterView(Context context) {
        super(context);
    }

    public FieldDateFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDateFilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_date_filter;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable LocalDate value) {
        return DateUtils.date(getContext(), value);
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }
}
