package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.ui.FontUtils;

public abstract class FieldSpinnerView<T> extends LinearLayout implements View.OnClickListener,
        AdapterView.OnItemClickListener, PopupWindow.OnDismissListener, FieldReadOnly {
    private final Typeface typeface = FontUtils.getTypefaceRegular(getContext());

    @BindView(R.id.textViewWrapper)
    View textViewWrapper;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindDimen(R.dimen.spinner_item_width)
    int spinnerItemWidth;

    @Setter
    private FieldSpinnerListener<T> fieldSpinnerListener;
    @Getter
    private T value;

    private ListPopupWindow popupWindow;

    public FieldSpinnerView(Context context) {
        super(context);
        init();
    }

    public FieldSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldSpinnerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), getLayoutResourceId(), this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setReadOnly(false);
    }

    public void setValue(T value) {
        if (this.value != value) {
            this.value = value;
            textView.setText(getTextForValue(value));
            valueChanged();
        }
    }

    protected void valueChanged() {
    }

    public boolean dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        popupWindow = null;
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == textViewWrapper) {
            dismissPopupWindow();
            View anchor = v;
            int width = spinnerItemWidth;
            int gravity = Gravity.END;

            popupWindow = new android.support.v7.widget.ListPopupWindow(getContext(), null, R.attr.actionOverflowMenuStyle, R.style.OverflowMenu);
            popupWindow.setWidth(width);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAdapter(getAdapter());
            popupWindow.setAnchorView(anchor);
            popupWindow.setDropDownGravity(gravity);
            popupWindow.setOnItemClickListener(this);
            popupWindow.setOnDismissListener(this);
            popupWindow.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismissPopupWindow();
        //noinspection unchecked
        T value = (T) parent.getAdapter().getItem(position);
        if (fieldSpinnerListener != null) {
            fieldSpinnerListener.onSpinnerItemClick(this, value);
        }
    }

    @Override
    public void onDismiss() {
        dismissPopupWindow();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        //noinspection deprecation
        textView.setTextAppearance(getContext(), readOnly ? R.style.SecondaryTextAppearance : R.style.PrimaryTextAppearance);
        textView.setTypeface(typeface);
        imageView.setVisibility(readOnly ? INVISIBLE : VISIBLE);
        textViewWrapper.setOnClickListener(readOnly ? null : this);
        textViewWrapper.setClickable(!readOnly);
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract String getTextForValue(@Nullable T value);

    protected abstract ListAdapter getAdapter();

    public interface FieldSpinnerListener<T> {
        void onSpinnerItemClick(FieldSpinnerView view, T item);
    }
}
