package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;

public abstract class EventDetailSpinnerView<T> extends LinearLayout implements
        AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener {
    @BindView(R.id.textView)
    TextView textView;

    @BindDimen(R.dimen.event_detail_text_width)
    int spinnerItemWidth;

    @Setter
    private OnValueChanged onValueChanged;
    @Getter
    private T value;

    private ListPopupWindow popupWindow;

    public EventDetailSpinnerView(Context context) {
        super(context);
        init();
    }

    public EventDetailSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailSpinnerView(Context context, AttributeSet attrs, int defStyle) {
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
    }

    public void setValue(T value) {
        if (this.value != value) {
            this.value = value;
            textView.setText(getTextForValue(value));
            if (onValueChanged != null) {
                onValueChanged.onValueChanged();
            }
        }
    }

    public boolean dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        popupWindow = null;
        return false;
    }

    @OnClick(R.id.textView)
    void onTextViewClick() {
        dismissPopupWindow();
        View anchor = textView;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismissPopupWindow();
        //noinspection unchecked
        T value = (T) parent.getAdapter().getItem(position);
        setValue(value);
    }

    @Override
    public void onDismiss() {
        dismissPopupWindow();
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract String getTextForValue(T value);

    protected abstract ListAdapter getAdapter();

    public interface OnValueChanged {
        void onValueChanged();
    }
}
