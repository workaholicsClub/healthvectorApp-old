package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import lombok.Setter;
import ru.android.healthvector.R;

public abstract class FieldSpinnerView<T> extends BaseFieldValueView<T> implements View.OnClickListener,
        AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.textViewWrapper)
    View textViewWrapper;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindDimen(R.dimen.spinner_item_width)
    int spinnerItemWidth;

    private ListPopupWindow popupWindow;

    @Nullable
    @Setter
    private FieldSpinnerListener<T> fieldSpinnerListener;

    public FieldSpinnerView(Context context) {
        super(context);
    }

    public FieldSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldSpinnerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @CallSuper
    @Override
    protected void valueChanged() {
        textView.setText(getTextForValue(getValue()));
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
        imageView.setVisibility(readOnly ? INVISIBLE : VISIBLE);
        textViewWrapper.setOnClickListener(readOnly ? null : this);
        textViewWrapper.setClickable(!readOnly);
    }

    @Nullable
    protected abstract String getTextForValue(@Nullable T value);

    protected abstract ListAdapter getAdapter();

    public interface FieldSpinnerListener<T> {
        void onSpinnerItemClick(FieldSpinnerView view, T item);
    }
}
