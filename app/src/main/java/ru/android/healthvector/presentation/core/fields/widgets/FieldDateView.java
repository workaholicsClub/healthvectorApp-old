package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import org.joda.time.LocalDate;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.utils.strings.DateUtils;

public class FieldDateView extends FieldDialogView<LocalDate> {
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    public FieldDateView(Context context) {
        super(context);
    }

    public FieldDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.field_date;
    }

    @Nullable
    @Override
    protected String getTextForValue(@Nullable LocalDate value) {
        return DateUtils.date(getContext(), value);
    }

    public void setTitle(String text) {
        textViewTitle.setText(text);
    }
}
